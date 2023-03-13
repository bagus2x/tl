package bagus2x.tl.data.remote

import bagus2x.tl.BuildConfig
import bagus2x.tl.data.dto.AnnouncementDTO
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

const val BASE_URL = BuildConfig.TL_BASE_URL

class AnnouncementRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun save(
        description: String,
        file: File?
    ): AnnouncementDTO {
        val formData = formData {
            append("description", description)
            append(
                key = "file",
                value = file?.readBytes() ?: return@formData,
                headers = Headers.build {
                    append(
                        name = HttpHeaders.ContentType,
                        value = ContentType.MultiPart.FormData
                    )
                    append(
                        name = HttpHeaders.ContentDisposition,
                        value = "filename=\"${file.name}\""
                    )
                }
            )
        }
        val content = MultiPartFormDataContent(formData)
        return ktor(client) {
            post("$BASE_URL/announcement") {
                setBody(content)
            }.body()
        }
    }

    suspend fun getAnnouncements(authorId: Long?): List<AnnouncementDTO> {
        return ktor(client) {
            get("$BASE_URL/announcements") {
                if (authorId != null) {
                    parameter("authorId", authorId)
                }
            }.body()
        }
    }
}
