package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeMessageRepository
import bagus2x.tl.domain.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMessagesUseCaseTest {
    private lateinit var messageRepository: MessageRepository
    private lateinit var getMessagesUseCase: GetMessagesUseCase

    @Before
    fun setup() {
        messageRepository = FakeMessageRepository()
        getMessagesUseCase = GetMessagesUseCase(messageRepository)
    }

    @Test
    fun `get last messages returns list of messages`() = runTest {
        val messages = getMessagesUseCase("a", 1).first()
        println(messages)
        assertEquals(
            true,
            messages.isNotEmpty()
        )
    }
}
