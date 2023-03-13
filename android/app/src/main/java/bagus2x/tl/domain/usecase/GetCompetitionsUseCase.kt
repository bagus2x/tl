package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.flow.Flow

class GetCompetitionsUseCase(
    private val competitionRepository: CompetitionRepository,
) {

    operator fun invoke(
        query: String = "",
        order: String = "",
        filter: Map<String, List<String>> = emptyMap()
    ): Flow<List<Competition>> {
        return competitionRepository.getCompetitions(query, order, filter)
    }
}
