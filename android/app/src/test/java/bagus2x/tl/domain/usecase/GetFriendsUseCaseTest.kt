package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.data.FakeUserRepository
import bagus2x.tl.data.Users
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFriendsUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var getFriendsUseCase: GetFriendsUseCase

    @Before
    fun setup() {
        userRepository = FakeUserRepository(FakeAuthRepository())
        getFriendsUseCase = GetFriendsUseCase(userRepository)
    }

    @Test
    fun `get friends returns list of friends`() = runTest {
        assertEquals(
            Users.map { it.asModel() },
            getFriendsUseCase(1)
        )
    }
}
