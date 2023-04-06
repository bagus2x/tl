package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.data.FakeUserRepository
import bagus2x.tl.data.Users
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUsersUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        userRepository = FakeUserRepository(authRepository)
        getUsersUseCase = GetUsersUseCase(userRepository, authRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get users return list of users`() = runTest {
        val users = getUsersUseCase().first()
        assertEquals(Users.map { it.asModel() }, users)
    }
}
