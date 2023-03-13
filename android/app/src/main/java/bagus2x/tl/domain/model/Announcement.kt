package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class Announcement(
    val id: Long,
    val author: User,
    val description: String,
    val file: String?,
    val createdAt: LocalDateTime
)
