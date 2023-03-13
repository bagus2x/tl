package bagus2x.tl.data.utils

import bagus2x.tl.data.dto.ErrorDTO
import bagus2x.tl.data.local.AuthLocalDataSource
import bagus2x.tl.data.remote.AuthRemoteDataSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun HttpClient(authLocalDataSource: AuthLocalDataSource): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.ANDROID
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000 * 500
            connectTimeoutMillis = 1000 * 500
        }
        install(WebSockets)
        install(ContentNegotiation) {
            json()
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val token = authLocalDataSource.getAuth().firstOrNull()
                    val (accessToken, refreshToken) = token ?: return@loadTokens null
                    BearerTokens(accessToken, refreshToken)
                }
                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
                    try {
                        val res = AuthRemoteDataSource.refresh(client, refreshToken)
                        authLocalDataSource.save(res)
                        BearerTokens(res.accessToken, res.refreshToken)
                    } catch (e: ClientRequestException) {
                        throw e
                    }
                }
            }
        }
    }
}

val CHARSET = StandardCharsets.UTF_8.toString()

suspend inline fun HttpClient.get(
    urlString: String,
    query: String,
    order: String,
    filter: Map<String, List<String>>,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = get(urlString) {
    url {
        if (query.isNotBlank()) {
            parameters.append("query", URLEncoder.encode(query, CHARSET))
        }
        if (order.isNotBlank()) {
            parameters.append("order", URLEncoder.encode(order, CHARSET))
        }
        if (filter.isNotEmpty()) {
            parameters.append("filter", URLEncoder.encode(Json.encodeToString(filter), CHARSET))
        }
    }
    block()
}

suspend inline fun <T> ktor(client: HttpClient, block: HttpClient.() -> T): T {
    return try {
        client.tryClearToken()
        client.block()
    } catch (e: ClientRequestException) {
        val error = e.response.body<ErrorDTO>()
        error(error.message)
    } catch (e: ServerResponseException) {
        val error = e.response.body<ErrorDTO>()
        error(error.message)
    }
}

fun HttpClient.tryClearToken() {
    plugin(Auth).providers
        .filterIsInstance<BearerAuthProvider>()
        .firstOrNull()
        ?.clearToken()
}
