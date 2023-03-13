package bagus2x.tl.presentation.invitation.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.FilePicker
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TextField
import bagus2x.tl.presentation.common.components.TopBar
import java.io.File

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun InvitationFormScreen(
    navController: NavController,
    viewModel: InvitationFormViewModel = hiltViewModel()
) {
    val sent by viewModel.sent.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val file by viewModel.file.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val showSnackbar = LocalShowSnackbar.current
    val context = LocalContext.current
    LaunchedEffect(sent) {
        if (sent) {
            showSnackbar(context.getString(R.string.text_invitation_sent))
            navController.navigateUp()
        }
    }
    InvitationScreen(
        navigateUp = navController::navigateUp,
        send = viewModel::send,
        description = description,
        file = file,
        setFile = viewModel::setFile,
        setDescription = viewModel::setDescription,
        loading = loading
    )
}

@Composable
fun InvitationScreen(
    navigateUp: () -> Unit,
    send: () -> Unit,
    description: String,
    file: File?,
    setFile: (File) -> Unit,
    setDescription: (String) -> Unit,
    loading: Boolean
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.INVITATION_FORM_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_invitation_form),
                onBackClicked = navigateUp,
                onButtonClicked = send,
                buttonText = stringResource(R.string.text_send)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = description,
                onValueChange = setDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_DESCRIPTION),
                label = {
                    Text(text = stringResource(R.string.text_description))
                }
            )
            Text(
                text = "${description.length}/500",
                modifier = Modifier.align(Alignment.End)
            )
            FilePicker(
                selected = file,
                onSelected = setFile
            )
        }
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
