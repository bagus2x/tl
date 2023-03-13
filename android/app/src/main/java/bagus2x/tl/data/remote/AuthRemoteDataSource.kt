package bagus2x.tl.data.remote

import bagus2x.tl.data.dto.AuthDTO
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun signIn(email: String, password: String): AuthDTO {
        return ktor(client) {
            post("$BASE_URL/auth/signin") {
                contentType(ContentType.Application.Json)
                mapOf(
                    "email" to email,
                    "password" to password
                ).apply(::setBody)
            }.body()
        }
    }

    suspend fun signUp(name: String, email: String, password: String): AuthDTO {
        return ktor(client) {
            post("$BASE_URL/auth/signup") {
                contentType(ContentType.Application.Json)
                mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password
                ).apply(::setBody)
            }.body()
        }
    }

    suspend fun signOut() {
        return ktor(client) {
            delete("$BASE_URL/auth/signout") {
                contentType(ContentType.Application.Json)
            }.body<Unit>()
        }
    }

    suspend fun verify(verificationCode: String): AuthDTO {
        return ktor(client) {
            patch("$BASE_URL/auth/verification-code") {
                header("Verification-Code", verificationCode)
            }.body()
        }
    }

    companion object {

        context(RefreshTokensParams)suspend fun refresh(
            client: HttpClient,
            refreshToken: String
        ): AuthDTO {
            return client.get("$BASE_URL/auth/refresh") {
                markAsRefreshTokenRequest()
                contentType(ContentType.Application.Json)
                header("X-Refresh-Token", refreshToken)
            }.body()
        }
    }
}
