package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.MessageRepository
import java.io.File

class SendMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(receiverId: Long, description: String, file: File?) {
        if (file != null) {
            require(file.length() < (2 * 1024 * 1024))
        }
        require(description.length < 500)

        messageRepository.send(receiverId, description, file)
    }
}
