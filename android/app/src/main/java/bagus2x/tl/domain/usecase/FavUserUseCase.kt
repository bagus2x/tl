package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

class FavUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Long) {
        val user = userRepository.getUser(userId).filterNotNull().firstOrNull() ?: return
        if (user.favorite) {
            userRepository.unfavorite(userId)
            return
        }
        userRepository.favorite(userId)
    }
}
