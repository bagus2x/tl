package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.data.FakeUserRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFavUsersUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var getFavUsersUseCase: GetFavUsersUseCase

    @Before
    fun setup() {
        userRepository = FakeUserRepository(FakeAuthRepository())
        getFavUsersUseCase = GetFavUsersUseCase(userRepository)
    }

    @Test
    fun `get fav users returns list of favorite users`() = runTest {
        val users = getFavUsersUseCase(1)
        assertEquals(
            true,
            users.all { it.favorite }
        )
    }
}
