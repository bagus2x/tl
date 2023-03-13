package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class FriendUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Long, accepted: Boolean? = null) {
        if (accepted == true) {
            userRepository.acceptFriendship(userId)
            return
        }
        if (accepted == false) {
            userRepository.cancelFriendship(userId)
            return
        }

        val user = userRepository.getUser(userId).filterNotNull().first()

        if (user.status == "friendship_requested" || user.status == "friendship_accepted") {
            userRepository.cancelFriendship(userId)
            return
        }

        userRepository.requestFriendship(userId)
    }
}
