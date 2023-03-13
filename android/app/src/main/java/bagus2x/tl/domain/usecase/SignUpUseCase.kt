package bagus2x.tl.domain.usecase

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import bagus2x.tl.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(name: String, email: String, password: String) {
        require(name.length in 5..64) {
            "Nama harus lebih dari 5 dan kurang dari 64"
        }
        require(password.length in 5..64) {
            "Password harus lebih dari 5 dan kurang dari 64"
        }
        require(EMAIL_ADDRESS.matcher(email).matches()) {
            "Email harus valid"
        }

        authRepository.signUp(
            name = name,
            email = email.lowercase(),
            password = password
        )
    }

    suspend fun verify(code: String) {
        authRepository.verify(code)
    }
}
