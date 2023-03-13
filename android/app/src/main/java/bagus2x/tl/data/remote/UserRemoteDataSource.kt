package bagus2x.tl.data.remote

import bagus2x.tl.data.dto.UserDTO
import bagus2x.tl.data.utils.get
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class UserRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getUser(userId: Long): UserDTO {
        return ktor(client) {
            get("$BASE_URL/user/$userId").body()
        }
    }

    suspend fun getUsers(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): List<UserDTO> {
        return ktor(client) {
            get("$BASE_URL/users", query, order, filter).body()
        }
    }

    suspend fun getFriends(userId: Long): List<UserDTO> {
        return ktor(client) {
            get("$BASE_URL/user/$userId/friends").body()
        }
    }

    suspend fun getFavorites(userId: Long): List<UserDTO> {
        return ktor(client) {
            get("$BASE_URL/user/$userId/favorites").body()
        }
    }

    suspend fun favorite(userId: Long) {
        return ktor(client) {
            post("$BASE_URL/user/$userId/favorite").body()
        }
    }

    suspend fun unfavorite(userId: Long) {
        return ktor(client) {
            delete("$BASE_URL/user/$userId/favorite").body()
        }
    }

    suspend fun update(
        name: String,
        photo: File?,
        university: String,
        department: String,
        faculty: String,
        studyProgram: String,
        stream: String,
        batch: Int?,
        gender: String,
        age: Int?,
        bio: String,
        skills: List<String>,
        achievements: List<String>,
        certifications: List<String>,
        invitable: Boolean,
        location: String
    ): UserDTO {
        val formData = formData(
            name,
            photo,
            university,
            department,
            faculty,
            studyProgram,
            stream,
            batch,
            gender,
            age,
            bio,
            skills,
            achievements,
            certifications,
            invitable,
            location
        )
        val content = MultiPartFormDataContent(formData)

        return ktor(client) {
            put("$BASE_URL/user") {
                setBody(content)
            }.body()
        }
    }

    suspend fun requestFriendship(userId: Long) {
        return ktor(client) {
            post("$BASE_URL/user/${userId}/friend")
        }
    }

    suspend fun acceptFriendship(userId: Long) {
        return ktor(client) {
            patch("$BASE_URL/user/${userId}/friend")
        }
    }

    suspend fun cancelFriendship(userId: Long) {
        return ktor(client) {
            delete("$BASE_URL/user/${userId}/friend")
        }
    }
}

private fun formData(
    name: String,
    photo: File?,
    university: String,
    department: String,
    faculty: String,
    studyProgram: String,
    stream: String,
    batch: Int?,
    gender: String,
    age: Int?,
    bio: String,
    skills: List<String>,
    achievements: List<String>,
    certifications: List<String>,
    invitable: Boolean,
    location: String
): List<PartData> {
    return formData {
        append("name", name)
        if (photo != null) {
            append(
                key = "photo",
                value = photo.readBytes(),
                headers = Headers.build {
                    append(
                        name = HttpHeaders.ContentType,
                        value = ContentType.MultiPart.FormData
                    )
                    append(
                        name = HttpHeaders.ContentDisposition,
                        value = "filename=\"${photo.name}\""
                    )
                }
            )
        }
        append("university", university)
        append("faculty", faculty)
        append("department", department)
        append("studyProgram", studyProgram)
        append("stream", stream)
        if (batch != null) {
            append("batch", batch)
        }
        append("gender", gender)
        if (age != null) {
            append("age", age)
        }
        append("bio", bio)
        append("skills", Json.encodeToString(skills))
        append("achievements", Json.encodeToString(achievements))
        append("certifications", Json.encodeToString(certifications))
        append("invitable", "$invitable")
        append("location", location)
    }
}
