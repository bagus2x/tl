package bagus2x.tl.data

import bagus2x.tl.domain.model.Auth
import bagus2x.tl.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeAuthRepository : AuthRepository {
    private val auth = MutableStateFlow<Auth?>(null)

    override suspend fun signUp(name: String, email: String, password: String) {
        auth.update {
            Auth(
                userId = 1,
                accessToken = "fake-token",
                refreshToken = "fake-token",
                verified = false
            )
        }
    }

    override suspend fun signIn(email: String, password: String) {
        auth.update {
            Auth(
                userId = 1,
                accessToken = "fake-token",
                refreshToken = "fake-token",
                verified = false
            )
        }
    }

    override suspend fun signOut() {
        auth.update { null }
    }

    override suspend fun verify(verificationCode: String) {
        auth.update { it?.copy(verified = true) }
    }

    override fun getAuth(): Flow<Auth?> {
        return auth
    }
}
