package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.flow.Flow

class GetCompetitionUseCase(
    private val competitionRepository: CompetitionRepository,
) {

    operator fun invoke(competitionId: Long): Flow<Competition?> {
        return competitionRepository.getCompetition(competitionId)
    }
}
