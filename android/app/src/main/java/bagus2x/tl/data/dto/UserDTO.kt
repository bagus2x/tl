package bagus2x.tl.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import bagus2x.tl.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
@Entity(tableName = "user")
data class UserDTO(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @SerialName("name")
    @ColumnInfo(name = "name")
    val name: String,
    @SerialName("email")
    @ColumnInfo(name = "email")
    val email: String,
    @SerialName("photo")
    @ColumnInfo(name = "photo")
    val photo: String?,
    @SerialName("university")
    @ColumnInfo(name = "university")
    val university: String?,
    @SerialName("faculty")
    @ColumnInfo(name = "faculty")
    val faculty: String?,
    @SerialName("department")
    @ColumnInfo(name = "department")
    val department: String?,
    @SerialName("studyProgram")
    @ColumnInfo(name = "study_program")
    val studyProgram: String?,
    @SerialName("stream")
    @ColumnInfo(name = "stream")
    val stream: String?,
    @SerialName("batch")
    @ColumnInfo(name = "batch")
    val batch: Int?,
    @SerialName("gender")
    @ColumnInfo(name = "gender")
    val gender: String?,
    @SerialName("age")
    @ColumnInfo(name = "age")
    val age: Int?,
    @SerialName("bio")
    @ColumnInfo(name = "bio")
    val bio: String?,
    @SerialName("achievements")
    @ColumnInfo(name = "achievements")
    val achievements: List<String> = emptyList(),
    @SerialName("certifications")
    @ColumnInfo(name = "certifications")
    val certifications: List<String> = emptyList(),
    @SerialName("skills")
    @ColumnInfo(name = "skills")
    val skills: List<String> = emptyList(),
    @SerialName("invitable")
    @ColumnInfo(name = "invitable")
    val invitable: Boolean,
    @SerialName("friendshipStatus")
    @ColumnInfo(name = "friendship_status")
    val friendshipStatus: String?,
    @SerialName("location")
    @ColumnInfo(name = "location")
    val location: String?,
    @SerialName("favorite")
    @ColumnInfo(name = "favorite")
    val favorite: Boolean,
    @SerialName("rating")
    @ColumnInfo(name = "rating")
    val rating: Float,
    @SerialName("votes")
    @ColumnInfo(name = "votes")
    val votes: Int,
    @SerialName("likes")
    @ColumnInfo(name = "likes")
    val likes: Int,
    @SerialName("friends")
    @ColumnInfo(name = "friends")
    val friends: Int,
    @SerialName("createdAt")
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @SerialName("updatedAt")
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
) {
    fun asModel(): User {
        return User(
            id = id,
            name = name,
            email = email,
            photo = photo,
            university = university,
            department = department,
            faculty = faculty,
            studyProgram = studyProgram,
            stream = stream,
            batch = batch,
            gender = gender,
            age = age,
            bio = bio,
            skills = skills,
            achievements = achievements,
            certifications = certifications,
            invitable = invitable,
            location = location,
            status = friendshipStatus,
            favorite = favorite,
            rating = rating,
            votes = votes,
            likes = likes,
            friends = friends,
            createdAt = Instant
                .ofEpochMilli(createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }
}
