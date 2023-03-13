package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetUsersUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    operator fun invoke(
        query: String = "",
        order: String = "",
        filter: Map<String, List<String>> = emptyMap()
    ): Flow<List<User>> {
        return userRepository
            .getUsers(query, order, filter)
            .map {
                it.filter { user ->
                    val authId = authRepository
                        .getAuth()
                        .firstOrNull()
                        ?.userId
                    user.id != authId
                }
            }
    }
}
