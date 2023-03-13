package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun Dialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    shape: Shape = MaterialTheme.shapes.medium,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                modifier = modifier,
                color = backgroundColor,
                contentColor = contentColor,
                shape = shape,
                content = content
            )
        }
    }
}

@Composable
fun Dialog(
    visible: Boolean,
    message: String,
    positiveText: String,
    onPositiveClicked: () -> Unit,
    negativeText: String,
    onNegativeClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    shape: Shape = MaterialTheme.shapes.medium,
    properties: DialogProperties = DialogProperties()
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                modifier = modifier,
                color = backgroundColor,
                contentColor = contentColor,
                shape = shape
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.body2,
                        color = Color.White
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(
                            onClick = onNegativeClicked,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = negativeText)
                        }
                        Button(
                            onClick = onPositiveClicked,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = positiveText)
                        }
                    }
                }
            }
        }
    }
}
