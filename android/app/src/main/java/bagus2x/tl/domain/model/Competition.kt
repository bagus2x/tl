package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class Competition(
    val id: Long,
    val authorId: Long,
    val poster: String,
    val title: String,
    val description: String,
    val theme: String,
    val city: String,
    val country: String,
    val deadline: LocalDateTime,
    val minimumFee: Long,
    val maximumFee: Long,
    val category: String,
    val organizer: String,
    val organizerName: String,
    val urlLink: String,
    val favorite: Boolean,
    val createdAt: LocalDateTime,
)
