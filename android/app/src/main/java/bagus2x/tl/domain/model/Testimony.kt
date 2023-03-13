package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class Testimony(
    val id: Long,
    val author: User,
    val rating: Float,
    val description: String,
    val createdAt: LocalDateTime
)
