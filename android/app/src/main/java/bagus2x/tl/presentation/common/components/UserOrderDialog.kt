package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.tl.R

@Composable
fun UserOrderDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    options: List<String>,
    order: String,
    onChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selected by remember(order, visible) { mutableStateOf(order) }
    Dialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = 240.dp),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Column(
            modifier = Modifier.padding(start = 4.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            for (label in options) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selected == label,
                        onCLick = { selected = label },
                        text = {
                            Text(
                                text = when (label) {
                                    "name_asc" -> stringResource(R.string.text_name_asc)
                                    "name_desc" -> stringResource(R.string.text_name_desc)
                                    "batch_asc" -> stringResource(R.string.text_batch_asc)
                                    "batch_desc" -> stringResource(R.string.text_batch_desc)
                                    else -> label
                                }
                            )
                        }
                    )
                }
            }
            Button(
                onClick = {
                    onChange(selected)
                    onDismissRequest()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(R.string.text_save))
            }
        }
    }
}
