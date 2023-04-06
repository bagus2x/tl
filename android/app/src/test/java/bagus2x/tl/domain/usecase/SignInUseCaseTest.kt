package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        signInUseCase = SignInUseCase(authRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `sign in throws exception when email is not valid`() = runTest {
        signInUseCase(
            email = "bagus",
            password = "bagus123"
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `sign in throws exception when password is less than 5`() = runTest {
        signInUseCase(
            email = "bagus",
            password = "a"
        )
    }

    @Test
    fun `sign in success`() = runTest {
        signInUseCase(
            email = "bagus@gmail.com",
            password = "bagus123"
        )
        assertTrue(authRepository.getAuth().first() != null)
    }
}
