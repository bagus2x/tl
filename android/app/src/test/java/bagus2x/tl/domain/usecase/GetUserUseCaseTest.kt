package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.data.FakeUserRepository
import bagus2x.tl.data.Users
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetUserUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        userRepository = FakeUserRepository(authRepository)
        getUserUseCase = GetUserUseCase(authRepository, userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get users return list of users`() = runTest {
        val expected = Users.find { it.id == 2L }!!.asModel()
        val user = getUserUseCase(2).first()
        assertEquals(
            expected,
            user
        )
    }
}
