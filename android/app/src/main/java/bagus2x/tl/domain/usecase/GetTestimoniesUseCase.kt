package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Testimony
import bagus2x.tl.domain.repository.TestimonyRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetTestimoniesUseCase(
    private val testimonyRepository: TestimonyRepository
) {

    operator fun invoke(receiverId: Long): Flow<List<Testimony>> {
        return testimonyRepository.getTestimonies(receiverId)
    }
}
