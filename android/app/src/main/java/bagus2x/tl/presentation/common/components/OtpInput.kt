package bagus2x.tl.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpInput(
    length: Int,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val code = remember(value) {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = code.value,
        onValueChange = { onChange(it.text) },
        modifier = modifier.focusRequester(focusRequester = focusRequester),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            Box(modifier = Modifier.fillMaxWidth()) {
                CodeInputDecoration(
                    modifier = Modifier.align(Alignment.Center),
                    code = code.value.text,
                    length = length
                )
            }
        }
    )
}

@Composable
private fun CodeInputDecoration(
    modifier: Modifier = Modifier,
    code: String,
    length: Int
) {
    Box(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 0 until length) {
                val text = if (i < code.length) code[i].toString() else ""
                CodeEntry(
                    text = text,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(64.dp)
                )
            }
        }
    }
}

@Composable
private fun CodeEntry(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val color = animateColorAsState(
            targetValue = if (text.isEmpty()) MaterialTheme.colors.onBackground.copy(alpha = .5f)
            else MaterialTheme.colors.primary
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onSurface
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 6.dp, end = 6.dp, bottom = 8.dp)
                .height(2.dp)
                .fillMaxWidth()
                .background(color.value)
        )
    }
}
