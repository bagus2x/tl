package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class Invitation(
    val id: Long,
    val inviter: User,
    val invitee: User,
    val description: String,
    val response: String,
    val status: String,
    val file: String?,
    val createdAt: LocalDateTime
)
