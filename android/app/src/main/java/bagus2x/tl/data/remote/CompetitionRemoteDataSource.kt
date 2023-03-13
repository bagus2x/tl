package bagus2x.tl.data.remote

import bagus2x.tl.data.dto.CompetitionDTO
import bagus2x.tl.data.utils.get
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

class CompetitionRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun save(
        poster: File,
        title: String,
        description: String,
        theme: String,
        city: String,
        country: String,
        deadline: Long,
        minimumFee: Long,
        maximumFee: Long,
        category: String,
        organizer: String,
        organizerName: String,
        urlLink: String
    ): CompetitionDTO {
        val formData = formData {
            append(
                key = "poster",
                value = poster.readBytes(),
                headers = Headers.build {
                    append(
                        name = HttpHeaders.ContentType,
                        value = ContentType.MultiPart.FormData
                    )
                    append(
                        name = HttpHeaders.ContentDisposition,
                        value = "filename=\"${poster.name}\""
                    )
                })
            append("title", title)
            append("description", description)
            append("theme", theme)
            append("city", city)
            append("country", country)
            append("deadline", deadline)
            append("minimumFee", minimumFee)
            append("maximumFee", maximumFee)
            append("category", category)
            append("organizer", organizer)
            append("organizerName", organizerName)
            append("urlLink", urlLink)
        }
        val content = MultiPartFormDataContent(formData)

        return ktor(client) {
            post("$BASE_URL/competition") {
                setBody(content)
            }.body()
        }
    }

    suspend fun getCompetitions(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): List<CompetitionDTO> {
        return ktor(client) {
            get("$BASE_URL/competitions", query, order, filter).body()
        }
    }

    suspend fun getCompetition(competitionId: Long): CompetitionDTO {
        return ktor(client) {
            get("$BASE_URL/competition/$competitionId").body()
        }
    }

    suspend fun getFavorites(userId: Long): List<CompetitionDTO> {
        return ktor(client) {
            get("$BASE_URL/competitions/favorites/$userId").body()
        }
    }

    suspend fun favorite(competitionId: Long) {
        return ktor(client) {
            post("$BASE_URL/competition/$competitionId/favorite").body()
        }
    }

    suspend fun unfavorite(competitionId: Long) {
        return ktor(client) {
            delete("$BASE_URL/competition/$competitionId/favorite").body()
        }
    }
}
