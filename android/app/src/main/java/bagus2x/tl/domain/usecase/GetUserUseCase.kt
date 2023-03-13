package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserUseCase(
    private val authUseCase: AuthRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(userId: Long? = null): Flow<User?> {
        if (userId != null) {
            return userRepository.getUser(userId)
        }
        return authUseCase
            .getAuth()
            .filterNotNull()
            .flatMapLatest { auth ->
                userRepository.getUser(userId = auth.userId)
            }
    }
}
