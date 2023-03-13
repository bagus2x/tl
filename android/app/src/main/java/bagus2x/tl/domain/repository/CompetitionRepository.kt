package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Competition
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.time.LocalDateTime

interface CompetitionRepository {

    suspend fun save(
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
    )

    fun getCompetitions(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<Competition>>

    fun getCompetition(competitionId: Long): Flow<Competition?>

    suspend fun getFavorites(userId: Long): List<Competition>

    suspend fun favorite(competitionId: Long)

    suspend fun unfavorite(competitionId: Long)
}
