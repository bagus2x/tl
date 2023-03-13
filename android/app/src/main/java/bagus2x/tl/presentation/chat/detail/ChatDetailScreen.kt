package bagus2x.tl.presentation.chat.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.domain.model.Message
import bagus2x.tl.domain.model.User
import bagus2x.tl.presentation.chat.contact.component.UserInput
import bagus2x.tl.presentation.common.Utils
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.components.MessageList
import bagus2x.tl.presentation.common.components.Scaffold
import coil.compose.AsyncImage

@Composable
fun ChatDetailScreen(
    navController: NavController,
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }

    val user by viewModel.user.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val description by viewModel.newMessage.collectAsState()
    val loading by viewModel.loading.collectAsState()
    ChatDetailScreen(
        user = user,
        messages = messages,
        newMessage = description,
        loading = loading,
        navigateUp = navController::navigateUp,
        navigateToSearch = {
            user?.id?.let { userId ->
                navController.navigate("search/messages?user_id=$userId")
            }
        },
        setNewMessage = viewModel::setNewMessage,
        send = viewModel::send,
    )
}

@Composable
fun ChatDetailScreen(
    user: User?,
    navigateToSearch: () -> Unit,
    messages: List<Message>,
    newMessage: String,
    loading: Boolean,
    navigateUp: () -> Unit,
    setNewMessage: (String) -> Unit,
    send: () -> Unit
) {
    Scaffold(
        topBar = {
            if (user != null) {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = user.photo ?: Utils.profile(user.name),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(text = user.name)
                        }
                    },
                    actions = {
                        IconButton(onClick = navigateToSearch) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        }
    ) {
        val lazyListState = rememberLazyListState()
        LaunchedEffect(messages.size) {
            lazyListState.animateScrollToItem(0)
        }
        Column {
            MessageList(
                messages = messages,
                userId = user?.id,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
                state = lazyListState
            )
            UserInput(
                modifier = Modifier
                    .imePadding()
                    .padding(8.dp)
                    .fillMaxWidth(),
                description = newMessage,
                onChangeDescription = setNewMessage,
                onSendClicked = send
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
