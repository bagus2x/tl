package bagus2x.tl.data

import bagus2x.tl.TestUtils
import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.flow.*
import java.io.File
import java.time.LocalDateTime

val Competitions = LongArray(10) { it + 1L }
    .map {
        Competition(
            id = it,
            authorId = 1,
            poster = "https://1.bp.blogspot.com/-XRwk9z96EwE/XxwthF1qRrI/AAAAAAAABFU/lsFMmf4NNuUoprsZQHzclPhEQojhTeAWwCLcBGAsYHQ/s1600/6C4B71C3-1BD9-4EA2-B237-505792FA5992.jpeg",
            title = TestUtils.getRandomWords(2),
            description = TestUtils.getRandomWords(20),
            theme = listOf("Software Development", "UI/UX").random(),
            city = listOf("Malang", "Surabaya", "Jember").random(),
            country = "Indonesia",
            deadline = LocalDateTime.now().plusMonths(4),
            minimumFee = 50_000,
            maximumFee = 100_000,
            category = listOf("Nasional", "Internasional").random(),
            organizer = listOf("Organizer X", "Organizer Y").random(),
            organizerName = listOf("Organizer C", "Organizer K").random(),
            urlLink = "https://linkedin.com",
            favorite = it % 2 == 0L,
            createdAt = LocalDateTime.now().minusHours(it)
        )
    }
    .sortedByDescending { it.createdAt }

class FakeCompetitionRepository : CompetitionRepository {
    private val competitions = MutableStateFlow(Competitions)

    override suspend fun save(
        poster: File,
        title: String,
        description: String,
        theme: String,
        city: String,
        country: String,
        deadline: LocalDateTime,
        minimumFee: Long,
        maximumFee: Long,
        category: String,
        organizer: String,
        organizerName: String,
        urlLink: String
    ) {
        val newCompetitions = Competition(
            id = Competitions.last().id + 1,
            authorId = 1,
            poster = poster.path,
            title = title,
            description = description,
            theme = theme,
            city = city,
            country = country,
            deadline = LocalDateTime.now().plusMonths(4),
            minimumFee = 50_000,
            maximumFee = 100_000,
            category = category,
            organizer = organizer,
            organizerName = organizerName,
            urlLink = urlLink,
            favorite = false,
            createdAt = LocalDateTime.now()
        )
        competitions.update { (it + newCompetitions).sortedByDescending { competition -> competition.createdAt } }
    }

    override fun getCompetitions(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<Competition>> {
        return competitions
    }

    override fun getCompetition(competitionId: Long): Flow<Competition?> {
        return competitions.map { competitions -> competitions.find { it.id == competitionId } }
    }

    override suspend fun getFavorites(userId: Long): List<Competition> {
        return competitions
            .map { it.filter { competition -> competition.favorite } }
            .firstOrNull()
            ?: emptyList()
    }

    override suspend fun favorite(competitionId: Long) {
        competitions.update { competitions ->
            competitions.map { if (competitionId == it.id) it.copy(favorite = true) else it }
        }
    }

    override suspend fun unfavorite(competitionId: Long) {
        competitions.update { competitions ->
            competitions.map { if (competitionId == it.id) it.copy(favorite = false) else it }
        }
    }
}
