package bagus2x.tl.data

import bagus2x.tl.data.local.CompetitionLocalDataSource
import bagus2x.tl.data.remote.CompetitionRemoteDataSource
import bagus2x.tl.data.utils.networkBoundResource
import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.repository.CompetitionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId

class CompetitionRepositoryImpl(
    private val localDataSource: CompetitionLocalDataSource,
    private val remoteDataSource: CompetitionRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : CompetitionRepository {

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
        urlLink: String,
    ) = withContext(dispatcher) {
        val res = remoteDataSource.save(
            poster = poster,
            title = title,
            description = description,
            theme = theme,
            city = city,
            country = country,
            deadline = deadline
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli(),
            minimumFee = minimumFee,
            maximumFee = maximumFee,
            category = category,
            organizer = organizer,
            organizerName = organizerName,
            urlLink = urlLink
        )
        localDataSource.save(res)
    }

    override fun getCompetitions(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<Competition>> {
        return networkBoundResource(
            local = {
                localDataSource.getCompetitions(query, order, filter)
            },
            remote = {
                remoteDataSource.getCompetitions(query, order, filter)
            },
            update = { res ->
                localDataSource.save(res)
            }
        )
            .map { it.map { dto -> dto.asModel() } }
            .flowOn(dispatcher)
    }

    override fun getCompetition(
        competitionId: Long
    ): Flow<Competition?> {
        return networkBoundResource(
            local = {
                localDataSource.getCompetition(competitionId)
            },
            remote = {
                remoteDataSource.getCompetition(competitionId)
            },
            update = { res ->
                localDataSource.save(res)
            }
        )
            .map { it?.asModel() }
            .flowOn(dispatcher)
    }

    override suspend fun getFavorites(
        userId: Long
    ): List<Competition> {
        return withContext(dispatcher) {
            remoteDataSource
                .getFavorites(userId)
                .map { it.asModel() }
        }
    }

    override suspend fun favorite(competitionId: Long) {
        withContext(dispatcher) {
            remoteDataSource.favorite(competitionId)
            val competition = localDataSource
                .getCompetition(competitionId)
                .firstOrNull()
                ?: return@withContext
            localDataSource.save(competition.copy(favorite = true))
        }
    }

    override suspend fun unfavorite(competitionId: Long) {
        withContext(dispatcher) {
            remoteDataSource.unfavorite(competitionId)
            val competition = localDataSource
                .getCompetition(competitionId)
                .firstOrNull()
                ?: return@withContext
            localDataSource.save(
                competition.copy(favorite = false)
            )
        }
    }
}
