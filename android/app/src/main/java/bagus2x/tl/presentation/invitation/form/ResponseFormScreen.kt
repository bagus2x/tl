package bagus2x.tl.presentation.invitation.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TextField
import bagus2x.tl.presentation.common.components.TopBar

@Composable
fun ResponseFormScreen(
    navController: NavController,
    viewModel: ResponseFormViewModel = hiltViewModel()
) {
    val submitted by viewModel.submitted.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    val context = LocalContext.current
    LaunchedEffect(submitted) {
        if (submitted) {
            showSnackbar(context.getString(R.string.text_response_sent))
            navController.navigateUp()
        }
    }
    val response by viewModel.response.collectAsState()
    val loading by viewModel.loading.collectAsState()
    ResponseFormScreen(
        navigateUp = navController::navigateUp,
        send = viewModel::send,
        response = response,
        setResponse = viewModel::setResponse,
        loading = loading
    )
}

@Composable
fun ResponseFormScreen(
    navigateUp: () -> Unit,
    send: () -> Unit,
    response: String,
    setResponse: (String) -> Unit,
    loading: Boolean
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.RESPONSE_FORM_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_response_form),
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
                value = response,
                onValueChange = setResponse,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_DESCRIPTION),
                label = {
                    Text(text = "Description")
                }
            )
            Text(
                text = "${response.length}/500",
                modifier = Modifier.align(Alignment.End)
            )
        }
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
