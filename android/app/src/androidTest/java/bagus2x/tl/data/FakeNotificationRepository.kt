package bagus2x.tl.data

import bagus2x.tl.domain.model.Notification
import bagus2x.tl.domain.repository.NotificationRepository
import bagus2x.tl.presentation.common.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

val Notifications = buildList {
    add(
        Users.random().let { user ->
            Notification(
                id = 1,
                dataId = 1,
                title = user.name,
                subtitle1 = user.department,
                subtitle2 = "${user.university} ${user.batch}",
                photo = Utils.profile(user.name),
                description = "invitation_requested",
                createdAt = LocalDateTime.now()
            )
        }
    )
    add(
        Users.random().let { user ->
            Notification(
                id = 2,
                dataId = null,
                title = user.name,
                subtitle1 = user.department,
                subtitle2 = "${user.university} ${user.batch}",
                photo = Utils.profile(user.name),
                description = "friendship_requested",
                createdAt = LocalDateTime.now(),
            )
        }
    )
}

class FakeNotificationRepository : NotificationRepository {
    private val notifications = MutableStateFlow(Notifications)

    override fun getNotifications(): Flow<List<Notification>> {
        return notifications
    }

    override fun countUnread(): Flow<Int> {
        return flow { emit(0) }
    }
}
