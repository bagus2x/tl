package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Invitation
import java.io.File

interface InvitationRepository {

    suspend fun create(
        inviteeId: Long,
        description: String,
        file: File?
    )

    suspend fun respond(
        invitationId: Long,
        response: String,
        status: String
    )

    suspend fun getInvitation(invitationId: Long): Invitation
}
