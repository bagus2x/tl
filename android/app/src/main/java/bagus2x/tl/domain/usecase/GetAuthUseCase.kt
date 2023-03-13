package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Auth
import bagus2x.tl.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetAuthUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<Auth?> {
        return authRepository.getAuth()
    }
}
