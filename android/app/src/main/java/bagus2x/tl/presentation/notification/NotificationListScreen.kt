package bagus2x.tl.presentation.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Notification
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.Dialog
import bagus2x.tl.presentation.common.components.NotificationItem
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TopBar
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun NotificationListScreen(
    navController: NavController,
    viewModel: NotificationListViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }
    val notifications by viewModel.notifications.collectAsState()
    NotificationListScreen(
        navigateUp = navController::navigateUp,
        notifications = notifications,
        navigateToInvitationDetail = { invitationId ->
            navController.navigate("invitation_detail/$invitationId")
        },
        navigateToResponseDetail = { invitationId ->
            navController.navigate("response_detail/$invitationId")
        },
        navigateToFavorites = {
            navController.navigate("favorites")
        },
        respondFriendship = viewModel::respondFriendshipRequest,
        navigateToSearch = {
            navController.navigate("search/notifications")
        },
    )
}

@Composable
fun NotificationListScreen(
    navigateUp: () -> Unit,
    notifications: List<Notification>,
    navigateToInvitationDetail: (Long) -> Unit,
    navigateToResponseDetail: (Long) -> Unit,
    navigateToFavorites: () -> Unit,
    respondFriendship: (Long, Boolean) -> Unit,
    navigateToSearch: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.NOTIFICATION_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_notification),
                onBackClicked = navigateUp,
                onFavoritesClicked = navigateToFavorites,
                onNotificationClicked = {},
                onSearchClicked = navigateToSearch
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = notifications,
                key = { it.id }
            ) { notification ->
                var isDialogVisible by remember { mutableStateOf(false) }
                NotificationItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    photo = notification.photo,
                    title = notification.title,
                    subtitle1 = notification.subtitle1,
                    subtitle2 = notification.subtitle2,
                    text = notification.notificationText,
                    createdAt = notification.createdAt,
                    onClick = {
                        if (notification.dataId == null) {
                            return@NotificationItem
                        }
                        if (notification.description == "invitation_accepted" || notification.description == "invitation_declined") {
                            navigateToResponseDetail(notification.dataId)
                        }
                        if (notification.description == "friendship_requested") {
                            isDialogVisible = true
                        }
                        if (notification.description == "invitation_requested") {
                            navigateToInvitationDetail(notification.dataId)
                        }
                    }
                )
                Dialog(
                    visible = isDialogVisible,
                    onDismissRequest = { isDialogVisible = false },
                    message = stringResource(
                        R.string.text_dialog_friendship_response,
                        notification.title
                    ),
                    negativeText = stringResource(R.string.text_decline),
                    onNegativeClicked = {
                        respondFriendship(
                            notification.dataId ?: return@Dialog,
                            false
                        )
                        isDialogVisible = false
                    },
                    positiveText = stringResource(R.string.text_accept),
                    onPositiveClicked = {
                        respondFriendship(
                            notification.dataId ?: return@Dialog,
                            true
                        )
                        isDialogVisible = false
                    }
                )
                Divider()
            }
        }
        if (notifications.isEmpty()) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.not_found))
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                iterations = LottieConstants.IterateForever,
            )
        }
    }
}

private val Notification.notificationText: String
    @Composable
    get() = when (description) {
        "friendship_requested" -> stringResource(id = R.string.text_friendship_requested)
        "friendship_accepted" -> stringResource(id = R.string.text_friendship_accepted)
        "invitation_requested" -> stringResource(id = R.string.text_invitation_requested)
        "invitation_accepted" -> stringResource(id = R.string.text_invitation_accepted)
        "invitation_declined" -> stringResource(id = R.string.text_invitation_declined)
        else -> description
    }
