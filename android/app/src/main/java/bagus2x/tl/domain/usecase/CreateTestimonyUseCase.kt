package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.TestimonyRepository

class CreateTestimonyUseCase(
    private val testimonyRepository: TestimonyRepository
) {

    suspend operator fun invoke(receiverId: Long, description: String, rating: Float) {
        require(rating in 0.0..1.0)
        require(description.length <= 500) {
            "Deskripsi wajib kurang dari 500 karakter"
        }
        testimonyRepository.save(receiverId, description, rating)
    }
}
