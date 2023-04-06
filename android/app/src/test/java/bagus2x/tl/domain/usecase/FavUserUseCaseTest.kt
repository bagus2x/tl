package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.data.FakeUserRepository
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavUserUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var favUserUseCase: FavUserUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        userRepository = FakeUserRepository(authRepository)
        favUserUseCase = FavUserUseCase(userRepository)
    }

    @Test
    fun `favorite and unfavorite user`() = runTest {
        var user = userRepository.getUsers("", "", emptyMap()).first().find { !it.favorite }!!
        favUserUseCase(userId = user.id)
        user = userRepository.getUsers("", "", emptyMap()).first().find { it.id == user.id }!!
        assertEquals(true, user.favorite)
        favUserUseCase(userId = user.id)
        user = userRepository.getUsers("", "", emptyMap()).first().find { it.id == user.id }!!
        assertEquals(false, user.favorite)
    }
}
