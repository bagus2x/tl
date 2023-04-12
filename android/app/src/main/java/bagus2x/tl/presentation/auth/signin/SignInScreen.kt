package bagus2x.tl.presentation.auth.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.TextField

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsStateWithLifecycle()
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }

    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    SignInScreen(
        email = email,
        password = password,
        setEmail = viewModel::setEmail,
        setPassword = viewModel::setPassword,
        signIn = viewModel::signIn,
        navigateToSignUp = {
            navController.navigate("signup")
        },
        loading = loading,
    )
}

@Composable
fun SignInScreen(
    email: String,
    password: String,
    loading: Boolean = false,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
    navigateToSignUp: () -> Unit,
    signIn: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .testTag(Tag.SIGNIN_SCREEN),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.text_sign_in_sign_up),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = email,
                onValueChange = setEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_EMAIL),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                label = { Text(text = stringResource(R.string.text_email)) },
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = password,
                onValueChange = setPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_PASSWORD),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send,
                ),
                maxLines = 1,
                label = { Text(text = stringResource(R.string.text_password)) },
                keyboardActions = KeyboardActions(onSend = { signIn() }),
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = signIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.BTN_SIGNIN),
                enabled = !loading && email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.text_sign_in))
            }
            Text(
                text = stringResource(R.string.text_no_account),
                modifier = Modifier
                    .clickable(onClick = navigateToSignUp)
                    .testTag(Tag.BTN_NAVIGATE_TO_SIGN_UP)
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

