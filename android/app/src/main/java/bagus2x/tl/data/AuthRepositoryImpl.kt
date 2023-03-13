package bagus2x.tl.data

import bagus2x.tl.data.local.AuthLocalDataSource
import bagus2x.tl.data.remote.AuthRemoteDataSource
import bagus2x.tl.domain.model.Auth
import bagus2x.tl.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource,
    private val clearAllTables: () -> Unit,
    private val dispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String) {
        withContext(dispatcher) {
            val dto = remoteDataSource.signUp(name, email, password)
            localDataSource.save(dto)
        }
    }

    override suspend fun signIn(email: String, password: String) {
        withContext(dispatcher) {
            val dto = remoteDataSource.signIn(email, password)
            localDataSource.save(dto)
        }
    }

    override suspend fun signOut() {
        withContext(dispatcher) {
            remoteDataSource.signOut()
            localDataSource.delete()
            clearAllTables()
        }
    }

    override suspend fun verify(verificationCode: String) {
        withContext(dispatcher) {
            val dto = remoteDataSource.verify(verificationCode)
            localDataSource.save(dto)
        }
    }

    override fun getAuth(): Flow<Auth?> {
        return localDataSource
            .getAuth().map { it?.asModel() }
            .flowOn(dispatcher)
    }
}
