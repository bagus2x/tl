package bagus2x.tl.presentation.invitation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Invitation
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.*

@Composable
fun InvitationDetailScreen(
    navController: NavController,
    viewModel: InvitationDetailViewModel = hiltViewModel()
) {
    val invitation by viewModel.invitation.collectAsState()
    InvitationDetailScreen(
        invitation = invitation,
        navigateUp = navController::navigateUp,
        decline = { invitationId ->
            navController
                .navigate("response_form/$invitationId/false") {
                popUpTo("notifications")
            }
        },
        accept = { invitationId ->
            navController
                .navigate("response_form/$invitationId/true") {
                popUpTo("notifications")
            }
        }
    )
}

@Composable
fun InvitationDetailScreen(
    invitation: Invitation?,
    navigateUp: () -> Unit,
    decline: (Long) -> Unit,
    accept: (Long) -> Unit
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.INVITATION_DETAIL_SCREEN),
        topBar = {
            var isDialogVisible by remember { mutableStateOf(false) }
            TopBar(
                titleText = stringResource(R.string.text_invitation),
                buttonText = stringResource(R.string.text_respond),
                onButtonClicked = { isDialogVisible = true },
                onBackClicked = navigateUp,
                buttonEnabled = invitation?.response.isNullOrEmpty()
            )
            if (invitation != null) {
                Dialog(
                    visible = isDialogVisible,
                    onDismissRequest = { isDialogVisible = false },
                    message = stringResource(
                        R.string.text_dialog_invitation_response,
                        invitation.inviter.name
                    ),
                    negativeText = stringResource(R.string.text_decline),
                    onNegativeClicked = {
                        decline(invitation.id)
                    },
                    positiveText = stringResource(R.string.text_accept),
                    onPositiveClicked = {
                        accept(invitation.id)
                    }
                )
            }
        }
    ) {
        if (invitation != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UserProfile(
                    photo = invitation.inviter.photo,
                    name = invitation.inviter.name,
                    department = invitation.inviter.department,
                    university = invitation.inviter.university,
                    batch = invitation.inviter.batch,
                    timestamp = invitation.createdAt
                )
                Text(
                    text = invitation.description,
                    style = MaterialTheme.typography.body2
                )
                if (!invitation.file.isNullOrBlank()) {
                    AsyncImage(
                        model = invitation.file,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
