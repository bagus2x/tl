package bagus2x.tl.presentation.main

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bagus2x.tl.presentation.common.LocalAuthUser
import bagus2x.tl.presentation.common.LocalBlurScaffold
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.LocalUnreadNotifications
import bagus2x.tl.presentation.main.components.BottomBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val showSnackbar: (String) -> Unit = remember {
        { message: String ->
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
        }
    }
    var blurRadius by remember { mutableStateOf(0.dp) }
    val blurScaffold: (Dp) -> Unit = remember {
        { radius ->
            blurRadius = radius
        }
    }
    Scaffold(
        modifier = modifier
            .systemBarsPadding()
            .blur(radius = blurRadius),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.imePadding()
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            BottomBar(
                currentHierarchy = {
                    currentDestination
                        ?.hierarchy
                        ?.map { it.route }
                        ?.toList()
                        ?: emptyList()
                },
                onClickItem = { route ->
                    navController.navigate(route) {
                        val startDestId = navController.graph.findStartDestination().id
                        popUpTo(startDestId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }
    ) { contentPadding ->
        val unreadNotifications by viewModel.unreadNotifications.collectAsStateWithLifecycle()
        val auth by viewModel.auth.collectAsStateWithLifecycle()
        CompositionLocalProvider(
            LocalShowSnackbar provides showSnackbar,
            LocalUnreadNotifications provides unreadNotifications,
            LocalBlurScaffold provides blurScaffold,
            LocalAuthUser provides auth
        ) {
            NavGraph(
                navController = navController,
                auth = auth,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}
