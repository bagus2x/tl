package bagus2x.tl.presentation.user.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.User
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.*
import bagus2x.tl.presentation.common.components.Scaffold
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun UsersListScreen(
    navController: NavController,
    viewModel: UsersListViewModel = hiltViewModel()
) {
    val showSnackbar = LocalShowSnackbar.current
    val snackbar by viewModel.snackbar.collectAsStateWithLifecycle()
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }

    val users by viewModel.users.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val order by viewModel.order.collectAsState()
    UsersListScreen(
        users = users,
        filter = filter,
        setFilter = viewModel::setFilter,
        order = order,
        setOrder = viewModel::setOrder,
        favoriteUser = viewModel::toggleFavorite,
        navigateToUserDetail = { userId ->
            navController.navigate("user_detail/$userId")
        },
        navigateToSearch = {
            navController.navigate("search/users")
        },
        navigateToFavorites = {
            navController.navigate("favorites")
        },
        navigateToNotifications = {
            navController.navigate("notifications")
        }
    )
}

@Composable
fun UsersListScreen(
    users: List<User>,
    filter: Map<String, Map<String, Boolean>>,
    setFilter: (Map<String, Map<String, Boolean>>) -> Unit,
    order: String,
    setOrder: (String) -> Unit,
    favoriteUser: (Long) -> Unit,
    navigateToUserDetail: (Long) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToNotifications: () -> Unit
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.USERS_LIST_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_users),
                onSearchClicked = navigateToSearch,
                onFavoritesClicked = navigateToFavorites,
                onNotificationClicked = navigateToNotifications
            )
        }
    ) {
        UserList(
            users = users,
            onItemClicked = navigateToUserDetail,
            onFavClicked = favoriteUser,
            modifier = Modifier.fillMaxSize()
        )
        var filterDialogVisible by remember { mutableStateOf(false) }
        var sorterDialogVisible by remember { mutableStateOf(false) }
        FilterOrderButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            onFilterClicked = { filterDialogVisible = true },
            onSorterClicked = { sorterDialogVisible = true }
        )
        UserOrderDialog(
            visible = sorterDialogVisible,
            onChange = setOrder,
            onDismissRequest = { sorterDialogVisible = false },
            options = listOf(
                "name_asc",
                "name_desc",
                "batch_asc",
                "batch_desc"
            ),
            order = order
        )
        UserFilterDialog(
            visible = filterDialogVisible,
            onDismissRequest = { filterDialogVisible = false },
            onChange = setFilter,
            options = filter.ifEmpty {
                mapOf(
                    "study_program" to stringArrayResource(R.array.text_study_program_list).associateWith { false },
                    "stream" to stringArrayResource(R.array.text_stream_list).associateWith { false },
                    "batch" to stringArrayResource(R.array.text_batch_list).associateWith { false }
                )
            }
        )
    }
}
