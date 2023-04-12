package bagus2x.tl.presentation.chat.contact

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.User
import bagus2x.tl.presentation.common.components.ContactItem

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ContactScreen(
    navController: NavController,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    ContactScreen(
        navigateUp = navController::navigateUp,
        users = users,
        navigateToChatDetail = { userId ->
            navController.navigate("chat_detail/$userId") {
                popUpTo("contact") { inclusive = true }
            }
        }
    )
}

@Composable
fun ContactScreen(
    users: List<User>,
    navigateUp: () -> Unit,
    navigateToChatDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.text_contact))
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(modifier = Modifier.padding(contentPadding)) {
            items(users) { user ->
                ContactItem(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    photo = user.photo,
                    name = user.name,
                    onClick = { navigateToChatDetail(user.id) }
                )
            }
        }
    }
}
