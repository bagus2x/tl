package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeTestimonyRepository
import bagus2x.tl.domain.repository.TestimonyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTestimonyUseCaseTest {
    private lateinit var testimonyRepository: TestimonyRepository
    private lateinit var createTestimonyUseCase: CreateTestimonyUseCase

    @Before
    fun setup() {
        testimonyRepository = FakeTestimonyRepository()
        createTestimonyUseCase = CreateTestimonyUseCase(testimonyRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `add announcement throws exception when descriptions is greater than 500`() = runTest {
        createTestimonyUseCase(
            description = "this is description".repeat(500),
            rating = .5f,
            receiverId = 1
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `add announcement throws exception when rating is greater than 1`() = runTest {
        createTestimonyUseCase(
            description = "this is description",
            rating = 1.5f,
            receiverId = 1
        )
    }

    @Test
    fun `add announcement success`() = runTest {
        createTestimonyUseCase(
            description = "this is description",
            rating = .5f,
            receiverId = 1
        )
        assertEquals(
            true,
            testimonyRepository.getTestimonies(1)
                .first()
                .find { it.description == "this is description" } != null
        )
    }
}
