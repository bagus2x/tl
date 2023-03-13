package bagus2x.tl.presentation.chat.contact.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.BasicTextField

@Composable
fun UserInput(
    modifier: Modifier = Modifier,
    description: String,
    onChangeDescription: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1F)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colors.onBackground.copy(0.1F)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = description,
                onValueChange = onChangeDescription,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1F)
                    .testTag(Tag.TF_DESCRIPTION),
                placeholder = {
                    Text(text = "Message here...")
                },
            )
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        }
        Surface(
            contentColor = MaterialTheme.colors.primary
        ) {
            IconButton(onClick = onSendClicked) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null
                )
            }
        }
    }
}
