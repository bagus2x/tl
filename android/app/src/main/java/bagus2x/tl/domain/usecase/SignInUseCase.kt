package bagus2x.tl.domain.usecase

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import bagus2x.tl.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String) {
        require(password.length in 5..64) {
            "Password harus lebih dari 5 dan kurang dari 64"
        }
        require(EMAIL_ADDRESS.matcher(email).matches()) {
            "Email harus valid"
        }

        authRepository.signIn(email, password)
    }
}
