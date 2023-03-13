package bagus2x.tl.data.remote

import bagus2x.tl.data.dto.TestimonyDTO
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class TestimonyRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun save(receiverId: Long, description: String, rating: Float): TestimonyDTO {
        val content = JsonObject(
            content = mapOf(
                "receiverId" to JsonPrimitive(receiverId),
                "description" to JsonPrimitive(description),
                "rating" to JsonPrimitive(rating)
            )
        )

        return ktor(client) {
            post("$BASE_URL/testimony") {
                contentType(ContentType.Application.Json)
                setBody(content)
            }.body()
        }
    }

    suspend fun getTestimonies(receiverId: Long): List<TestimonyDTO> {
        return ktor(client) {
            get("$BASE_URL/testimonies/$receiverId") {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }
}
