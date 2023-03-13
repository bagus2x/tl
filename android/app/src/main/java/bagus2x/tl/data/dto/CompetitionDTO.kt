package bagus2x.tl.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import bagus2x.tl.domain.model.Competition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Serializable
@Entity(tableName = "competition")
data class CompetitionDTO(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @SerialName("authorId")
    @ColumnInfo(name = "author_id")
    val authorId: Long,
    @SerialName("poster")
    @ColumnInfo(name = "poster")
    val poster: String,
    @SerialName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerialName("description")
    @ColumnInfo(name = "description")
    val description: String,
    @SerialName("theme")
    @ColumnInfo(name = "theme")
    val theme: String,
    @SerialName("city")
    @ColumnInfo(name = "city")
    val city: String,
    @SerialName("country")
    @ColumnInfo(name = "country")
    val country: String,
    @SerialName("deadline")
    @ColumnInfo(name = "deadline")
    val deadline: Long,
    @SerialName("minimumFee")
    @ColumnInfo(name = "minimum_fee")
    val minimumFee: Long,
    @SerialName("maximumFee")
    @ColumnInfo(name = "maximum_fee")
    val maximumFee: Long,
    @SerialName("category")
    @ColumnInfo(name = "category")
    val category: String,
    @SerialName("organizer")
    @ColumnInfo(name = "organizer")
    val organizer: String,
    @SerialName("organizerName")
    @ColumnInfo(name = "organizer_name")
    val organizerName: String,
    @SerialName("urlLink")
    @ColumnInfo(name = "url_link")
    val urlLink: String,
    @SerialName("favorite")
    @ColumnInfo(name = "favorite")
    val favorite: Boolean,
    @SerialName("createdAt")
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
) {

    fun asModel(): Competition {
        return Competition(
            id = id,
            authorId = authorId,
            poster = poster,
            title = title,
            description = description,
            theme = theme,
            city = city,
            country = country,
            deadline = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(deadline),
                ZoneId.systemDefault()
            ),
            minimumFee = listOf(minimumFee, maximumFee).max(),
            maximumFee = listOf(minimumFee, maximumFee).min(),
            category = category,
            organizer = organizer,
            organizerName = organizerName,
            urlLink = urlLink,
            favorite = favorite,
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(createdAt),
                ZoneId.systemDefault()
            )
        )
    }
}
