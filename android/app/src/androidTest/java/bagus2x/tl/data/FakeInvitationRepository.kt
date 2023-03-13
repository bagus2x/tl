package bagus2x.tl.data

import bagus2x.tl.TestUtils
import bagus2x.tl.data.dto.InvitationDTO
import bagus2x.tl.domain.model.Invitation
import bagus2x.tl.domain.repository.InvitationRepository
import java.io.File

class FakeInvitationRepository : InvitationRepository {
    override suspend fun create(inviteeId: Long, description: String, file: File?) {}

    override suspend fun respond(invitationId: Long, response: String, status: String) {}

    override suspend fun getInvitation(invitationId: Long): Invitation {
        return InvitationDTO(
            id = invitationId,
            inviter = Users.find { it.id == 2L }!!,
            invitee = Users.find { it.id == 2L }!!,
            description = TestUtils.getRandomWords(50),
            response = "",
            status = "invitation_requested",
            file = null,
            createdAt = System.currentTimeMillis()
        ).asModel()
    }
}
