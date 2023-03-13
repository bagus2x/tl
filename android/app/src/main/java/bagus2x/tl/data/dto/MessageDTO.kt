package bagus2x.tl.data.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import bagus2x.tl.domain.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Serializable
@Entity(tableName = "message")
data class MessageDTO(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @SerialName("sender")
    @Embedded(prefix = "sender_")
    val sender: UserDTO,
    @SerialName("receiver")
    @Embedded(prefix = "receiver_")
    val receiver: UserDTO,
    @SerialName("description")
    @ColumnInfo(name = "description")
    val description: String,
    @SerialName("file")
    @ColumnInfo(name = "file")
    val file: String?,
    @SerialName("unread")
    @ColumnInfo(name = "unread")
    val unread: Boolean,
    @SerialName("totalUnread")
    @ColumnInfo(name = "total_unread")
    val totalUnread: Int,
    @SerialName("createdAt")
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "last_message")
    val lastMessage: Boolean = false
) {

    fun asModel(): Message {
        return Message(
            id = id,
            sender = sender.asModel(),
            receiver = receiver.asModel(),
            description = description,
            file = file,
            unread = unread,
            totalUnread = totalUnread,
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(createdAt),
                ZoneId.systemDefault()
            )
        )
    }
}
