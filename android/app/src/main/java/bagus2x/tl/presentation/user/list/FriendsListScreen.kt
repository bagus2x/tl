package bagus2x.tl.presentation.user.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.components.TopBar
import bagus2x.tl.presentation.common.components.UserList

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun FriendsListScreen(
    navController: NavController,
    viewModel: FriendsListViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_friend),
                onBackClicked = navController::navigateUp
            )
        }
    ) { contentPadding ->
        val users by viewModel.users.collectAsStateWithLifecycle()
        UserList(
            users = users,
            onItemClicked = { userId ->
                navController.navigate("user_detail/$userId")
            },
            onFavClicked = viewModel::toggleFavorite,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    }
}
