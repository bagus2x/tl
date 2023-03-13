package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bagus2x.tl.domain.model.Message
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun MessageList(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    reverseLayout: Boolean = true,
    userId: Long?
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()) }
    LazyColumn(
        modifier = modifier,
        state = state,
        verticalArrangement = verticalArrangement,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout
    ) {
        itemsIndexed(
            items = messages,
            key = { _, message -> message.id }
        ) { index, message ->
            MessageItem(
                modifier = Modifier.fillMaxWidth(),
                description = message.description,
                arrangementEnd = message.receiver.id != userId,
                createdAt = message.createdAt
            )
            val messageDate = rememberSaveable {
                val currentMessageDate = messages[index]
                    .createdAt
                    .toLocalDate()
                val prevMessageDate = messages
                    .getOrNull(index + 1)
                    ?.createdAt
                    ?.toLocalDate()

                if (prevMessageDate == null || prevMessageDate.isEqual(currentMessageDate).not())
                    currentMessageDate.format(formatter)
                else
                    ""
            }
            if (messageDate.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = messageDate,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}
