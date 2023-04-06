package bagus2x.tl.data

import bagus2x.tl.TestUtils
import bagus2x.tl.data.dto.TestimonyDTO
import bagus2x.tl.domain.model.Testimony
import bagus2x.tl.domain.repository.TestimonyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

val Testimonies = LongArray(100) { it + 1L }
    .map {
        TestimonyDTO(
            id = it,
            receiver = Users.random(),
            sender = Users.random(),
            rating = (1..5).random().toFloat(),
            description = TestUtils.getRandomWords(20),
            createdAt = System.currentTimeMillis()
        )
    }
    .sortedByDescending { it.createdAt }

class FakeTestimonyRepository : TestimonyRepository {
    private val testimonies = MutableStateFlow(Testimonies)

    override suspend fun save(receiverId: Long, description: String, rating: Float) {
        val newTestimony = TestimonyDTO(
            id = Testimonies.last().id + 1,
            receiver = Users.find { it.id == receiverId }!!,
            sender = Users.find { it.id == 1L }!!,
            rating = (1..5).random().toFloat(),
            description = description,
            createdAt = System.currentTimeMillis()
        )

        testimonies.update { testimonies -> (testimonies + newTestimony).sortedByDescending { it.createdAt } }
    }

    override fun getTestimonies(receiverId: Long): Flow<List<Testimony>> {
        return testimonies.map { testimonies ->
            testimonies.filter { it.receiver.id == receiverId }.map { it.asModel() }
        }
    }
}
