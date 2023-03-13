package bagus2x.tl.presentation.announcement.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.FilePicker
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TextField
import bagus2x.tl.presentation.common.components.TopBar
import java.io.File

@Composable
fun AnnouncementFormScreen(
    navController: NavController,
    viewModel: AnnouncementFormViewModel = hiltViewModel()
) {
    val submitted by viewModel.submitted.collectAsState()
    val snackbar by viewModel.snackbar.collectAsState()
    val snackbarState = LocalShowSnackbar.current
    LaunchedEffect(submitted, snackbar) {
        if (submitted) {
            navController.navigateUp()
        }
        if (snackbar.isNotBlank()) {
            snackbarState(snackbar)
            viewModel.snackbarConsumed()
        }
    }

    val description by viewModel.description.collectAsState()
    val file by viewModel.file.collectAsState()
    val loading by viewModel.loading.collectAsState()
    AnnouncementFormScreen(
        description = description,
        file = file,
        loading = loading,
        setDescription = viewModel::setDescription,
        setFile = viewModel::setFile,
        navigateUp = navController::navigateUp,
        post = viewModel::post
    )
}

@Composable
fun AnnouncementFormScreen(
    description: String,
    file: File?,
    loading: Boolean,
    navigateUp: () -> Unit,
    setDescription: (String) -> Unit,
    setFile: (File) -> Unit,
    post: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.ANNOUNCEMENT_FORM_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_add_announcement),
                onBackClicked = navigateUp,
                buttonText = stringResource(R.string.text_post),
                onButtonClicked = post,
                buttonEnabled = !loading && description.isNotEmpty()
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
