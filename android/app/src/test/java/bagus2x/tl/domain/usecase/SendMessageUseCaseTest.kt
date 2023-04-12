package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeMessageRepository
import bagus2x.tl.domain.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMessageUseCaseTest {
    private lateinit var messageRepository: MessageRepository
    private lateinit var sendMessageUseCase: SendMessageUseCase

    @Before
    fun setup() {
        messageRepository = FakeMessageRepository()
        sendMessageUseCase = SendMessageUseCase(messageRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `send message trows exception when description is greater than 500`() = runTest {
        sendMessageUseCase(
            receiverId = 1,
            description = "description".repeat(500),
            file = null
        )
    }

    @Test
    fun `send message success`() = runTest {
        sendMessageUseCase(
            receiverId = 1,
            description = "this is description",
            file = null
        )
        val newMessage = messageRepository.getMessages(1).first().first()
        assertEquals(
            "this is description",
            newMessage.description
        )
    }
}
