package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotifications(): Flow<List<Notification>>

    fun countUnread(): Flow<Int>
}
