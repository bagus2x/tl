package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeInvitationRepository
import bagus2x.tl.domain.repository.InvitationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RespondInvitationUseCaseTest {
    private lateinit var invitationRepository: InvitationRepository
    private lateinit var respondInvitationUseCase: RespondInvitationUseCase

    @Before
    fun setup() {
        invitationRepository = FakeInvitationRepository()
        respondInvitationUseCase = RespondInvitationUseCase(invitationRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `respond invitation throws exception when response is greater than 500`() = runTest {
        respondInvitationUseCase(
            invitationId = 1,
            response = "response".repeat(500),
            accepted = true
        )
    }

    @Test
    fun `respond invitation success`() = runTest {
        respondInvitationUseCase(
            invitationId = 1,
            response = "response",
            accepted = true
        )
    }
}
