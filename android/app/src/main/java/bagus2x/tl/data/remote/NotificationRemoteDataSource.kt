package bagus2x.tl.data.remote

import bagus2x.tl.BuildConfig
import bagus2x.tl.data.dto.NotificationDTO
import bagus2x.tl.data.utils.ktor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NotificationRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getNotifications(): List<NotificationDTO> {
        return ktor(client) {
            get("$BASE_URL/notifications").body()
        }
    }

    suspend fun countUnread(): Int {
        return ktor(client) {
            get("$BASE_URL/notifications/unread").body()
        }
    }

    companion object {
        private const val BASE_URL = BuildConfig.TL_BASE_URL
    }
}
