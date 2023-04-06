package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeCompetitionRepository
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFavCompetitionsUseCaseTest {
    private lateinit var competitionRepository: CompetitionRepository
    private lateinit var getFavCompetitionsUseCase: GetFavCompetitionsUseCase

    @Before
    fun setup() {
        competitionRepository = FakeCompetitionRepository()
        getFavCompetitionsUseCase = GetFavCompetitionsUseCase(competitionRepository)
    }

    @Test
    fun `get fav competitions returns list of favorite competitions`() = runTest {
        val competitions = getFavCompetitionsUseCase(1)
        assertEquals(
            true,
            competitions.all { it.favorite }
        )
    }
}
