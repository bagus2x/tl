package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Invitation
import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.repository.InvitationRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class GetInvitationUseCase(
    private val invitationRepository: InvitationRepository,
) {

    suspend operator fun invoke(invitationId: Long): Invitation {
        return invitationRepository.getInvitation(invitationId)
    }
}
