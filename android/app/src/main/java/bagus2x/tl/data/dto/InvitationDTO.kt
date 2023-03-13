package bagus2x.tl.data.dto

import bagus2x.tl.domain.model.Invitation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class InvitationDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("inviter")
    val inviter: UserDTO,
    @SerialName("invitee")
    val invitee: UserDTO,
    @SerialName("description")
    val description: String,
    @SerialName("response")
    val response: String,
    @SerialName("status")
    val status: String,
    @SerialName("file")
    val file: String?,
    @SerialName("createdAt")
    val createdAt: Long
) {

    fun asModel(): Invitation {
        return Invitation(
            id = id,
            inviter = inviter.asModel(),
            invitee = invitee.asModel(),
            description = description,
            response = response,
            status = status,
            file = file,
            createdAt = Instant
                .ofEpochMilli(createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }
}
