package bagus2x.tl.presentation.auth.signup

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
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.TextField

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val snackbarState = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            snackbarState(snackbar)
            viewModel.snackbarConsumed()
        }
    }
    val auth by viewModel.auth.collectAsState()
    val loading by viewModel.loading.collectAsState()
    if (auth?.verified == false) {
        val verificationCode by viewModel.code.collectAsState()
        VerificationScreen(
            verificationCode = verificationCode,
            setVerificationCode = viewModel::setVerificationCode,
            submit = viewModel::verify,
            loading = loading
        )
    } else {
        val name by viewModel.name.collectAsState()
        val email by viewModel.email.collectAsState()
        val password by viewModel.password.collectAsState()
        SignUpScreen(
            name = name,
            email = email,
            password = password,
            loading = loading,
            setName = viewModel::setName,
            setEmail = viewModel::setEmail,
            setPassword = viewModel::setPassword,
            signUp = viewModel::signUp,
            navigateToSignIn = {
                navController.navigate("signin")
            }
        )
    }
}

@Composable
fun SignUpScreen(
    name: String,
    email: String,
    password: String,
    loading: Boolean,
    setName: (String) -> Unit,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
    signUp: () -> Unit,
    navigateToSignIn: () -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .testTag(Tag.SIGNUP_SCREEN),
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
                value = name,
                onValueChange = setName,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_NAME),
                label = {
                    Text(text = stringResource(R.string.text_name))
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = email,
                onValueChange = setEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_EMAIL),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                label = {
                    Text(text = stringResource(R.string.text_email))
                },
                maxLines = 1
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
                keyboardActions = KeyboardActions(onSend = { signUp() }),
                label = {
                    Text(text = stringResource(R.string.text_password))
                },
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = signUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.BTN_SIGNUP),
                enabled = !loading && name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.text_sign_up))
            }
            Text(
                text = stringResource(R.string.text_have_account),
                modifier = Modifier
                    .clickable(onClick = navigateToSignIn)
                    .testTag(Tag.BTN_NAVIGATE_TO_SIGN_IN)
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
