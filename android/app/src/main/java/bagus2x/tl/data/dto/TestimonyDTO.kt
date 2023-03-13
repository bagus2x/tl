package bagus2x.tl.data.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import bagus2x.tl.domain.model.Testimony
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
@Entity(tableName = "testimony")
data class TestimonyDTO(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @SerialName("receiver")
    @Embedded(prefix = "receiver_")
    val receiver: UserDTO,
    @SerialName("sender")
    @Embedded(prefix = "sender_")
    val sender: UserDTO,
    @SerialName("description")
    @ColumnInfo(name = "description")
    val description: String,
    @SerialName("rating")
    @ColumnInfo(name = "rating")
    val rating: Float,
    @SerialName("createdAt")
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
) {
    fun asModel(): Testimony {
        return Testimony(
            id = id,
            author = sender.asModel(),
            description = description,
            rating = rating,
            createdAt = Instant
                .ofEpochMilli(createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }
}
