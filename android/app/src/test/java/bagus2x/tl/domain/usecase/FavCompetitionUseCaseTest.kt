package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeCompetitionRepository
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavCompetitionUseCaseTest {
    private lateinit var competitionRepository: CompetitionRepository
    private lateinit var favCompetitionUseCase: FavCompetitionUseCase

    @Before
    fun setup() {
        competitionRepository = FakeCompetitionRepository()
        favCompetitionUseCase = FavCompetitionUseCase(competitionRepository)
    }

    @Test
    fun `favorite and unfavorite competition`() = runTest{
        var competition = competitionRepository.getCompetitions("", "", emptyMap()).first().find { !it.favorite }!!
        favCompetitionUseCase(competitionId = competition.id)
        competition = competitionRepository.getCompetitions("", "", emptyMap()).first().find { it.id == competition.id }!!
        assertEquals(true, competition.favorite)
        favCompetitionUseCase(competitionId = competition.id)
        competition = competitionRepository.getCompetitions("", "", emptyMap()).first().find { it.id == competition.id }!!
        assertEquals(false, competition.favorite)
    }
}
