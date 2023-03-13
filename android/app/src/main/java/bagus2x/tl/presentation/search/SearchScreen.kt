package bagus2x.tl.presentation.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Announcement
import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.model.Message
import bagus2x.tl.domain.model.User
import bagus2x.tl.presentation.common.components.*

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    type: String
) {
    val query by viewModel.query.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val users by viewModel.users.collectAsState()
    val competitions by viewModel.competitions.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val lastMessages by viewModel.lastMessages.collectAsState()
    SearchScreen(
        type = type,
        query = query,
        messageUserId = viewModel.userId,
        setQuery = viewModel::setQuery,
        announcements = announcements,
        users = users,
        competitions = competitions,
        lastMessages = lastMessages,
        messages = messages,
        navigateToAnnouncementDetail = {},
        navigateToUserDetail = { userId ->
            navController.navigate("user_detail/$userId")
        },
        navigateToCompetitionDetail = { competitionId ->
            navController.navigate(
                "competition_detail/$competitionId"
            )
        },
        navigateToChatDetail = { userId ->
            navController.navigate("chat_detail/$userId")
        }
    )
}

@Composable
fun SearchScreen(
    type: String,
    query: String,
    messageUserId: Long? = null,
    setQuery: (String) -> Unit,
    announcements: List<Announcement>,
    users: List<User>,
    competitions: List<Competition>,
    lastMessages: List<Message>,
    messages: List<Message>,
    navigateToAnnouncementDetail: (Long) -> Unit,
    navigateToUserDetail: (Long) -> Unit,
    navigateToCompetitionDetail: (Long) -> Unit,
    navigateToChatDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = setQuery,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(text = stringResource(R.string.text_search))
                    }
                )
            }
        }
    ) {
        if (type == "announcements") {
            AnnouncementList(
                announcements = announcements,
                onItemClicked = navigateToAnnouncementDetail,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (type == "users") {
            UserList(
                users = users,
                onItemClicked = navigateToUserDetail,
                onFavClicked = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (type == "competitions") {
            CompetitionList(
                competitions = competitions,
                onItemClicked = navigateToCompetitionDetail,
                onFavClicked = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (type == "messages") {
            MessageList(
                messages = messages,
                userId = messageUserId,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (type == "last_messages") {
            LastMessageList(
                lastMessages = lastMessages,
                onItemClicked = navigateToChatDetail,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
