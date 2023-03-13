package bagus2x.tl.data.remote

import android.util.Base64
import android.util.Base64.encodeToString
import bagus2x.tl.BuildConfig
import bagus2x.tl.data.dto.MessageDTO
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File

private const val WS_BASE_URL = BuildConfig.TL_BASE_URL_WS

class MessageRemoteDataSource(
    private val client: HttpClient
) {
    private var send: (suspend (Frame) -> Unit)? = null

    suspend fun getMessages(receiverId: Long): List<MessageDTO> {
        return ktor(client) {
            get("$BASE_URL/messages/$receiverId") {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }

    suspend fun sendMessage(receiverId: Long, description: String, file: File?) {
        val req = JsonObject(
            content = mutableMapOf(
                "receiverId" to JsonPrimitive(receiverId),
                "description" to JsonPrimitive(description),
            ).apply {
                if (file != null) {
                    val base64 = encodeToString(file.readBytes(), Base64.NO_WRAP)
                    this["file"] = JsonPrimitive(base64)
                }
            }
        )
        val encoded = Json.encodeToString(req)
        try {
            send!!.invoke(Frame.Text(encoded))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun observeNewMessage(receiverId: Long): Flow<MessageDTO> {
        return callbackFlow {
            val socket = ktor(client) {
                webSocketSession("$WS_BASE_URL/message/chat/$receiverId")
            }

            send = socket::send

            launch {
                socket.incoming.consumeEach { frame ->
                    val json = (frame as? Frame.Text)?.readText() ?: ""
                    val res = Json.decodeFromString<MessageDTO>(json)
                    send(res)
                }
            }

            awaitClose { socket.cancel() }
        }
    }

    suspend fun getLastMessage(): List<MessageDTO> {
        return ktor(client) {
            get("$BASE_URL/messages/last") {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }
}
