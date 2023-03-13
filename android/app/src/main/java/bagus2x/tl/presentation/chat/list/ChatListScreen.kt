package bagus2x.tl.presentation.chat.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Message
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.components.LastMessageList
import bagus2x.tl.presentation.common.components.TopBar

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val lastMessages by viewModel.lastMessages.collectAsState()
    val snackbar by viewModel.snackbar.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }
    ChatListScreen(
        lastMessages = lastMessages,
        navigateToSearch = {
            navController.navigate("search/last_messages")
        },
        navigateToContact = {
            navController.navigate("contact")
        },
        navigateToChatDetail = { userId ->
            navController.navigate("chat_detail/$userId")
        }
    )
}

@Composable
fun ChatListScreen(
    lastMessages: List<Message>,
    navigateToSearch: () -> Unit,
    navigateToContact: () -> Unit,
    navigateToChatDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_chat),
                onSearchClicked = navigateToSearch,
                onAddClicked = navigateToContact
            )
        }
    ) { contentPadding ->
        LastMessageList(
            lastMessages = lastMessages,
            onItemClicked = navigateToChatDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        )
    }
}
