package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.repository.UserRepository

class GetFriendsUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Long): List<User> {
        return userRepository.getFriends(userId)
    }
}
