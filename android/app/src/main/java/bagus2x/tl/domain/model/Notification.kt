package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class Notification(
    val id: Long,
    val dataId: Long?,
    val title: String,
    val subtitle1: String?,
    val subtitle2: String?,
    val photo: String?,
    val description: String,
    val createdAt: LocalDateTime
)
