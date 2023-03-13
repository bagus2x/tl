package bagus2x.tl.presentation.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.OtpInput

@Composable
fun VerificationScreen(
    verificationCode: String,
    setVerificationCode: (String) -> Unit,
    submit: () -> Unit,
    loading: Boolean
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .testTag(Tag.VERIFICATION_SCREEN),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.text_verification_code_sent),
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = stringResource(R.string.text_input_4_digit_code),
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(32.dp))
            OtpInput(
                length = 4,
                value = verificationCode,
                onChange = setVerificationCode,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_VERIFICATION_CODE)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = submit,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.BTN_SUBMIT)
            ) {
                Text(text = stringResource(R.string.text_submit))
            }
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
