package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.repository.CompetitionRepository

class GetFavCompetitionsUseCase(
    private val competitionRepository: CompetitionRepository
) {

    suspend operator fun invoke(userId: Long): List<Competition> {
        return competitionRepository.getFavorites(userId)
    }
}
