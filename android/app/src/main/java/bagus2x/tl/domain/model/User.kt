package bagus2x.tl.domain.model

import java.time.LocalDateTime

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val photo: String?,
    val university: String?,
    val faculty: String?,
    val department: String?,
    val studyProgram: String?,
    val stream: String?,
    val batch: Int?,
    val gender: String?,
    val age: Int?,
    val bio: String?,
    val skills: List<String>,
    val achievements: List<String>,
    val certifications: List<String>,
    val invitable: Boolean,
    val location: String?,
    val favorite: Boolean,
    val rating: Float,
    val votes: Int,
    val likes: Int,
    val friends: Int,
    val status: String?,
    val createdAt: LocalDateTime
)
