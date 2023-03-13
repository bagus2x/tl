package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Message
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.MessageRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLastMessagesUseCase(
    private val messageRepository: MessageRepository,
) {

    operator fun invoke(query: String? = null): Flow<List<Message>> {
        return messageRepository
            .getLastMessages()
            .map { messages ->
                if (query == null) {
                    return@map messages
                }
                if (query.isBlank()) {
                    return@map emptyList()
                }
                messages.filter { message ->
                    val q = query.lowercase()
                    message.description.lowercase().contains(q)
                }
            }
    }
}
