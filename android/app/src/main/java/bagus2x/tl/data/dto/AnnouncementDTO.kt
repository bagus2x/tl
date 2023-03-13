package bagus2x.tl.data.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import bagus2x.tl.domain.model.Announcement
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant.*
import java.time.LocalDateTime.*
import java.time.ZoneId.*

@Serializable
@Entity(tableName = "announcement")
data class AnnouncementDTO(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @SerialName("author")
    @Embedded(prefix = "author_")
    val author: UserDTO,
    @SerialName("description")
    @ColumnInfo(name = "description")
    val description: String,
    @SerialName("file")
    @ColumnInfo(name = "file")
    val file: String?,
    @SerialName("createdAt")
    @ColumnInfo(name = "created_at")
    val createdAt: Long
) {

    fun asModel(): Announcement {
        return Announcement(
            id = id,
            author = author.asModel(),
            description = description,
            file = file,
            createdAt = ofInstant(ofEpochMilli(createdAt), systemDefault())
        )
    }
}
