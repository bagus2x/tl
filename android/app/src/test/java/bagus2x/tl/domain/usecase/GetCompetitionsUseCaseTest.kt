package bagus2x.tl.domain.usecase

import bagus2x.tl.data.Competitions
import bagus2x.tl.data.FakeCompetitionRepository
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCompetitionsUseCaseTest {
    private lateinit var competitionRepository: CompetitionRepository
    private lateinit var getCompetitionsUseCase: GetCompetitionsUseCase

    @Before
    fun setup() {
        competitionRepository = FakeCompetitionRepository()
        getCompetitionsUseCase = GetCompetitionsUseCase(competitionRepository)
    }

    @Test
    fun `get competitions returns list of competitions`() = runTest {
        val competitions = getCompetitionsUseCase()
        assertEquals(
            Competitions,
            competitions.first()
        )
    }
}
