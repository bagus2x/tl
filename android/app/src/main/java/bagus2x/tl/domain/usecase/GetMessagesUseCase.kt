package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Message
import bagus2x.tl.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMessagesUseCase(
    private val messageRepository: MessageRepository
) {

    operator fun invoke(query: String? = null, receiverId: Long): Flow<List<Message>> {
        return messageRepository
            .getMessages(receiverId)
            .map { messages ->
                if (query == null) {
                    return@map messages
                }
                if (query.isBlank()) {
                    return@map emptyList()
                }
                messages.filter { message ->
                    message.description.contains(query.lowercase())
                }
            }
    }
}
