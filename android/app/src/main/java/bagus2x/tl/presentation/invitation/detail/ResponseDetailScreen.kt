package bagus2x.tl.presentation.invitation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Invitation
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TopBar
import bagus2x.tl.presentation.common.components.UserProfile

@Composable
fun ResponseDetailScreen(
    navController: NavController,
    viewModel: ResponseDetailViewModel = hiltViewModel()
) {
    val invitation by viewModel.invitation.collectAsState()
    ResponseDetailScreen(
        invitation = invitation,
        navigateUp = navController::navigateUp
    )
}

@Composable
fun ResponseDetailScreen(
    invitation: Invitation?,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_response),
                onBackClicked = navigateUp
            )
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
                    text = invitation.response,
                    style = MaterialTheme.typography.body2
                )
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
