package bagus2x.tl.domain.usecase

import bagus2x.tl.data.Announcements
import bagus2x.tl.data.FakeAnnouncementRepository
import bagus2x.tl.domain.repository.AnnouncementRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAnnouncementsUseCaseTest {
    private lateinit var announcementRepository: AnnouncementRepository
    private lateinit var getAnnouncementsUseCase: GetAnnouncementsUseCase

    @Before
    fun setup() {
        announcementRepository = FakeAnnouncementRepository()
        getAnnouncementsUseCase = GetAnnouncementsUseCase(announcementRepository)
    }

    @Test
    fun `get announcements returns list of announcements`() = runTest {
        val announcements = getAnnouncementsUseCase().first()
        assertEquals(Announcements.map { it.asModel() }, announcements)
    }
}
