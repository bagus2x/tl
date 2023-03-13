package bagus2x.tl.data

import bagus2x.tl.data.local.AnnouncementLocalDataSource
import bagus2x.tl.data.remote.AnnouncementRemoteDataSource
import bagus2x.tl.data.utils.networkBoundResource
import bagus2x.tl.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class AnnouncementRepositoryImpl(
    private val localDataSource: AnnouncementLocalDataSource,
    private val remoteDataSource: AnnouncementRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : AnnouncementRepository {

    override suspend fun save(description: String, file: File?) =
        withContext(dispatcher) {
            val res = remoteDataSource.save(description, file)
            localDataSource.save(res)
        }

    override fun getAnnouncements(query: String, authorId: Long?) =
        networkBoundResource(
            local = {
                if (authorId != null) {
                    localDataSource.getAnnouncements(query, authorId)
                } else {
                    localDataSource.getAnnouncements(query)
                }
            },
            remote = {
                remoteDataSource.getAnnouncements(authorId)
            },
            update = localDataSource::save
        )
            .map { it.map { dto -> dto.asModel() } }
            .flowOn(dispatcher)
}
