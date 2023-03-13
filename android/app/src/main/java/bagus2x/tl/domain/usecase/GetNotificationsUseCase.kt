package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Notification
import bagus2x.tl.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {
    val unread = notificationRepository.countUnread()

    operator fun invoke(): Flow<List<Notification>> {
        return notificationRepository.getNotifications()
    }
}
