package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeInvitationRepository
import bagus2x.tl.domain.repository.InvitationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetInvitationUseCaseTest {
    private lateinit var invitationRepository: InvitationRepository
    private lateinit var getInvitationUseCase: GetInvitationUseCase

    @Before
    fun setup() {
        invitationRepository = FakeInvitationRepository()
        getInvitationUseCase = GetInvitationUseCase(invitationRepository)
    }

    @Test
    fun `get invitation returns invitation`() = runTest {
        assertEquals(
            1L,
            getInvitationUseCase(1).id
        )
    }
}
