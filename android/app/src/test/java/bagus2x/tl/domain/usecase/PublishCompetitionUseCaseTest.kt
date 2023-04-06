package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeCompetitionRepository
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import java.io.File
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class PublishCompetitionUseCaseTest {
    private lateinit var competitionRepository: CompetitionRepository
    private lateinit var publishCompetitionUseCase: PublishCompetitionUseCase

    @Before
    fun setup() {
        competitionRepository = FakeCompetitionRepository()
        publishCompetitionUseCase = PublishCompetitionUseCase(competitionRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `publish competition throws exception when title is blank`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(1 * 1024 * 1024)
        publishCompetitionUseCase(
            poster = file,
            title = "",
            description = "this is description",
            theme = "theme",
            city = "city",
            country = "country",
            deadline = LocalDateTime.now().plusDays(30),
            minimumFee = 10000,
            maximumFee = 200000,
            category = "IT",
            organizer = "University",
            organizerName = "Univ A",
            urlLink = "https://google.com"
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `publish competition throws exception when file is greater than 2mb`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(3 * 1024 * 1024)
        publishCompetitionUseCase(
            poster = file,
            title = "this is title",
            description = "this is description",
            theme = "theme",
            city = "city",
            country = "country",
            deadline = LocalDateTime.now().plusDays(30),
            minimumFee = 10000,
            maximumFee = 200000,
            category = "IT",
            organizer = "University",
            organizerName = "Univ A",
            urlLink = "https://google.com"
        )
    }

    @Test
    fun `publish competition success`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(1 * 1024 * 1024)
        `when`(file.path).thenReturn("")
        publishCompetitionUseCase(
            poster = file,
            title = "this is title",
            description = "this is description",
            theme = "theme",
            city = "city",
            country = "country",
            deadline = LocalDateTime.now().plusDays(30),
            minimumFee = 10000,
            maximumFee = 200000,
            category = "IT",
            organizer = "University",
            organizerName = "Univ A",
            urlLink = "https://google.com"
        )
    }
}
