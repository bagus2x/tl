package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeInvitationRepository
import bagus2x.tl.domain.repository.InvitationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class CreateInvitationUseCaseTest {
    private lateinit var invitationRepository: InvitationRepository
    private lateinit var createInvitationUseCase: CreateInvitationUseCase

    @Before
    fun setup() {
        invitationRepository = FakeInvitationRepository()
        createInvitationUseCase = CreateInvitationUseCase(invitationRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create invitation throws exception when descriptions is blank`() = runTest {
        createInvitationUseCase(
            inviteeId = 1,
            description = "",
            file = null
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create invitation throws exception when file is greater than 2mb`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(2 * 1024 * 1024)
        createInvitationUseCase(
            inviteeId = 1,
            description = "",
            file = file
        )
    }

    @Test
    fun `create invitation success`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(1 * 1024 * 1024)
        createInvitationUseCase(
            inviteeId = 1,
            description = "this is description",
            file = file
        )
    }
}
