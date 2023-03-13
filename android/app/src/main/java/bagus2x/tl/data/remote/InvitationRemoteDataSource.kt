package bagus2x.tl.data.remote

import bagus2x.tl.data.dto.InvitationDTO
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

class InvitationRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun create(
        inviteeId: Long,
        description: String,
        file: File?
    ) {
        val formData = formData {
            append("inviteeId", inviteeId)
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
            post("$BASE_URL/invitation") {
                setBody(content)
            }
        }
    }

    suspend fun respond(
        invitationId: Long,
        response: String,
        status: String
    ) {
        return ktor(client) {
            patch("$BASE_URL/invitation/$invitationId") {
                contentType(ContentType.Application.Json)
                mapOf(
                    "response" to response,
                    "status" to status
                ).apply(::setBody)
            }
        }
    }

    suspend fun getInvitation(
        invitationId: Long
    ): InvitationDTO {
        return ktor(client) {
            get("$BASE_URL/invitation/$invitationId").body()
        }
    }
}
