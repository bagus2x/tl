package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val sender: User,
    val receiver: User,
    val description: String,
    val file: String?,
    val unread: Boolean,
    val totalUnread: Int,
    val createdAt: LocalDateTime
)
