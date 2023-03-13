package bagus2x.tl.presentation.testimony

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
import bagus2x.tl.presentation.common.components.RatingBar
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TextField
import bagus2x.tl.presentation.common.components.TopBar

@Composable
fun TestimonyFormScreen(
    navController: NavController,
    viewModel: TestimonyFormViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val sent by viewModel.sent.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    val context = LocalContext.current
    LaunchedEffect(snackbar, sent) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
        if (sent) {
            showSnackbar(context.getString(R.string.text_testimony_sent))
            navController.navigateUp()
        }
    }

    val rating by viewModel.rating.collectAsState()
    val description by viewModel.description.collectAsState()
    val loading by viewModel.loading.collectAsState()
    TestimonyFormScreen(
        navigateUp = navController::navigateUp,
        send = viewModel::send,
        rating = rating,
        setRating = viewModel::setRating,
        description = description,
        setDescription = viewModel::setDescription,
        loading = loading
    )
}

@Composable
fun TestimonyFormScreen(
    navigateUp: () -> Unit,
    send: () -> Unit,
    rating: Float,
    setRating: (Float) -> Unit,
    description: String,
    setDescription: (String) -> Unit,
    loading: Boolean
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.TESTIMONY_FORM_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_testimony),
                onButtonClicked = send,
                buttonText = stringResource(R.string.text_send),
                onBackClicked = navigateUp
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RatingBar(
                rating = rating,
                onChange = { _, newRating ->
                    setRating(newRating)
                },
                max = 5,
                size = 32.dp
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_DESCRIPTION),
                value = description,
                onValueChange = setDescription,
                label = {
                    Text(text = stringResource(R.string.text_description))
                }
            )
            Text(
                text = "${description.length}/500",
                modifier = Modifier.align(Alignment.End)
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
