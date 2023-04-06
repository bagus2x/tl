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
class GetLastMessagesUseCaseTest {
    private lateinit var messageRepository: MessageRepository
    private lateinit var getLastMessagesUseCase: GetLastMessagesUseCase

    @Before
    fun setup() {
        messageRepository = FakeMessageRepository()
        getLastMessagesUseCase = GetLastMessagesUseCase(messageRepository)
    }

    @Test
    fun `get last messages returns list of last messages`() = runTest {
        val messages = getLastMessagesUseCase().first()
        assertEquals(
            true,
            messages.isNotEmpty()
        )
    }
}
