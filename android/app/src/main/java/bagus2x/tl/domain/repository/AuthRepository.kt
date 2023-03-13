package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Auth
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(name: String, email: String, password: String)

    suspend fun signIn(email: String, password: String)

    suspend fun signOut()

    suspend fun verify(verificationCode: String)

    fun getAuth(): Flow<Auth?>
}
