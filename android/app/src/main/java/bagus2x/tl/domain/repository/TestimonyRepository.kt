package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Testimony
import kotlinx.coroutines.flow.Flow

interface TestimonyRepository {

    suspend fun save(receiverId: Long, description: String, rating: Float)

    fun getTestimonies(receiverId: Long): Flow<List<Testimony>>
}
