package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

class FavCompetitionUseCase(
    private val competitionRepository: CompetitionRepository
) {

    suspend operator fun invoke(competitionId: Long) {
        val competition = competitionRepository
            .getCompetition(competitionId)
            .filterNotNull()
            .firstOrNull()
            ?: return
        if (competition.favorite) {
            competitionRepository.unfavorite(competitionId)
            return
        }
        competitionRepository.favorite(competitionId)
    }
}
