package bagus2x.tl.domain.usecase

import bagus2x.tl.data.Competitions
import bagus2x.tl.data.FakeCompetitionRepository
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCompetitionUseCaseTest {
    private lateinit var competitionRepository: CompetitionRepository
    private lateinit var getCompetitionUseCase: GetCompetitionUseCase

    @Before
    fun setup() {
        competitionRepository = FakeCompetitionRepository()
        getCompetitionUseCase = GetCompetitionUseCase(competitionRepository)
    }

    @Test
    fun `get competition returns competition`() = runTest {
        val expected = Competitions.find { it.id == 2L }!!
        val competition = getCompetitionUseCase(expected.id).filterNotNull().first()
        assertEquals(
            expected,
            competition
        )
    }
}

