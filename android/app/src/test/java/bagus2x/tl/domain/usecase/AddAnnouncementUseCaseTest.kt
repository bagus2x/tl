package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeAnnouncementRepository
import bagus2x.tl.domain.repository.AnnouncementRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class AddAnnouncementUseCaseTest {
    private lateinit var announcementRepository: AnnouncementRepository
    private lateinit var addAnnouncementUseCase: AddAnnouncementUseCase

    @Before
    fun setup() {
        announcementRepository = FakeAnnouncementRepository()
        addAnnouncementUseCase = AddAnnouncementUseCase(announcementRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `add announcement throws exception when descriptions is blank`() = runTest {
        addAnnouncementUseCase(
            description = "",
            file = null
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `add announcement throws exception when file is greater than 2mb`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(2 * 1024 * 1024)
        addAnnouncementUseCase(
            description = "this is description",
            file = file
        )
    }

    @Test
    fun `add announcement success`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(1 * 1024 * 1024)
        addAnnouncementUseCase(
            description = "this is description",
            file = file
        )
        assertEquals(
            true,
            announcementRepository.getAnnouncements("", null)
                .first()
                .find { it.description == "this is description" } != null
        )
    }
}
