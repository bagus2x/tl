package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAuthUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var getAuthUseCase: GetAuthUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        getAuthUseCase = GetAuthUseCase(authRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get auth returns auth tokens`() = runTest {
        authRepository.signIn("bagus@gmail.com", "bagus123")
        val auth = authRepository.getAuth().first()
        Assert.assertEquals(
            true,
            auth != null
        )
    }
}
