package bagus2x.tl.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import bagus2x.tl.TestUtils
import bagus2x.tl.data.dto.MessageDTO
import bagus2x.tl.domain.model.Message
import bagus2x.tl.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.io.File

private val users = Users.shuffled().take(2)
val Messages = LongArray(10) { it + 1L }
    .map {
        MessageDTO(
            id = it,
            receiver = users[0],
            sender = users[1],
            description = TestUtils.getRandomWords(200),
            createdAt = System.currentTimeMillis() - 1000,
            unread = false,
            totalUnread = 0,
            file = null
        )
    }
    .sortedByDescending { it.createdAt }

class FakeMessageRepository : MessageRepository {
    private val messages = MutableStateFlow(Messages)

    override suspend fun send(receiverId: Long, description: String, file: File?) {
        val newMessage = MessageDTO(
            id = Messages.last().id + 1,
            receiver = users[1],
            sender = users[0],
            description = description,
            createdAt = System.currentTimeMillis(),
            unread = false,
            totalUnread = 0,
            file = null
        )
        messages.update { messages -> (messages + newMessage).sortedByDescending { it.createdAt } }
    }

    override fun getMessages(receiverId: Long): Flow<List<Message>> {
        return messages.map { it.map { it.asModel() } }
    }

    override fun getLastMessages(): Flow<List<Message>> {
        return messages.map { it.take(1).map { it.asModel() } }
    }
}
