package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeTestimonyRepository
import bagus2x.tl.data.Testimonies
import bagus2x.tl.domain.repository.TestimonyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTestimoniesUseCaseTest {
    private lateinit var testimonyRepository: TestimonyRepository
    private lateinit var getTestimoniesUseCase: GetTestimoniesUseCase

    @Before
    fun setup() {
        testimonyRepository = FakeTestimonyRepository()
        getTestimoniesUseCase = GetTestimoniesUseCase(testimonyRepository)
    }

    @Test
    fun `get testimonies returns list of testimonies`() = runTest {
        val userId = Testimonies[0].receiver.id
        val testimonies = getTestimoniesUseCase(userId).first()
        assertEquals(true, testimonies.isNotEmpty())
    }
}
