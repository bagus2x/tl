package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SignUpUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        signUpUseCase = SignUpUseCase(authRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `sign up throws exception when email is not valid`() = runTest {
        signUpUseCase(
            email = "bagus",
            password = "bagus123",
            name = "bagus"
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `sign up throws exception when name is blank`() = runTest {
        signUpUseCase(
            email = "bagus",
            password = "bagus123",
            name = ""
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `sign up throws exception when password is less than 5`() = runTest {
        signUpUseCase(
            email = "bagus",
            password = "a",
            name = "Tubagus"
        )
    }

    @Test
    fun `sign up success`() = runTest {
        signUpUseCase(
            email = "bagus@gmail.com",
            password = "bagus123",
            name = "Tubagus"
        )
        assertTrue(!authRepository.getAuth().first()!!.verified)
        signUpUseCase.verify("8080")
        assertTrue(authRepository.getAuth().first()!!.verified)
    }
}
