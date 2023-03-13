package bagus2x.tl.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import bagus2x.tl.domain.model.Notification
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
@Entity(tableName = "notification")
data class NotificationDTO(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @SerialName("dataId")
    @ColumnInfo(name = "data_id")
    val dataId: Long?,
    @SerialName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerialName("subtitle1")
    @ColumnInfo(name = "subtitle1")
    val subtitle1: String?,
    @SerialName("subtitle2")
    @ColumnInfo(name = "subtitle2")
    val subtitle2: String?,
    @SerialName("photo")
    @ColumnInfo(name = "photo")
    val photo: String?,
    @SerialName("description")
    @ColumnInfo(name = "description")
    val description: String,
    @SerialName("createdAt")
    @ColumnInfo(name = "created_at")
    val createdAt: Long
) {
    fun asModel(): Notification {
        return Notification(
            id = id,
            dataId = dataId,
            title = title,
            subtitle1 = subtitle1,
            subtitle2 = subtitle2,
            photo = photo,
            description = description,
            createdAt = Instant
                .ofEpochMilli(createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }
}
