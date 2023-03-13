package bagus2x.tl.presentation.announcement.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Announcement
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.AnnouncementList
import bagus2x.tl.presentation.common.components.TopBar

@Composable
fun AnnouncementListScreen(
    navController: NavController,
    viewModel: AnnouncementListViewModel = hiltViewModel(),
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }

    val announcements by viewModel.announcements.collectAsState()
    AnnouncementListScreen(
        navigateToSearch = {
            navController.navigate("search/announcements")
        },
        navigateToFavorites = {
            navController.navigate("favorites")
        },
        navigateToNotifications = {
            navController.navigate("notifications")
        },
        navigateToAnnouncementForm = {
            navController.navigate("announcement_form")
        },
        announcements = announcements,
        navigateToInvitationForm = { userId ->
            navController.navigate("invitation_form/$userId")
        }
    )
}

@Composable
fun AnnouncementListScreen(
    announcements: List<Announcement>,
    navigateToSearch: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToAnnouncementForm: () -> Unit,
    navigateToInvitationForm: (Long) -> Unit,
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.ANNOUNCEMENT_LIST_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_home),
                onSearchClicked = navigateToSearch,
                onFavoritesClicked = navigateToFavorites,
                onNotificationClicked = navigateToNotifications
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAnnouncementForm,
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.testTag(Tag.BTN_ADD)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    ) { contentPadding ->
        AnnouncementList(
            announcements = announcements,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            onItemClicked = navigateToInvitationForm
        )
    }
}
