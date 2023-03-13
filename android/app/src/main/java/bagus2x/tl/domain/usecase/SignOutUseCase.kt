package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke() {
        authRepository.signOut()
    }
}
