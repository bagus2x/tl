package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeNotificationRepository
import bagus2x.tl.data.Notifications
import bagus2x.tl.domain.repository.NotificationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotificationsUseCaseTest {
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var getNotificationsUseCase: GetNotificationsUseCase

    @Before
    fun setup() {
        notificationRepository = FakeNotificationRepository()
        getNotificationsUseCase = GetNotificationsUseCase(notificationRepository)
    }

    @Test
    fun `get notifications returns list of notifications`() = runTest {
        val notifications = getNotificationsUseCase().first()
        assertEquals(
            Notifications,
            notifications
        )
    }
}
