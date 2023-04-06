package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAuthRepository
import bagus2x.tl.domain.repository.AuthRepository
import org.junit.Before

class SignOutUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var signOutUseCase: SignOutUseCase

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        signOutUseCase = SignOutUseCase(authRepository)
    }
}
