package bagus2x.tl.data

import bagus2x.tl.data.local.NotificationLocalDataSource
import bagus2x.tl.data.remote.NotificationRemoteDataSource
import bagus2x.tl.data.utils.networkBoundResource
import bagus2x.tl.domain.model.Notification
import bagus2x.tl.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

class NotificationRepositoryImpl(
    private val localDataSource: NotificationLocalDataSource,
    private val remoteDataSource: NotificationRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : NotificationRepository {

    override fun getNotifications(): Flow<List<Notification>> {
        return networkBoundResource(
            local = localDataSource::getNotifications,
            remote = remoteDataSource::getNotifications,
            update = { res ->
                localDataSource.save(res)
            }
        )
            .map { dto -> dto.map { it.asModel() } }
            .flowOn(dispatcher)
    }

    override fun countUnread(): Flow<Int> {
        return flow {
            while (true) {
                try {
                    val unread = remoteDataSource.countUnread()
                    emit(unread)
                } catch (e: Exception) {
                    Timber.e(e)
                }
                delay(2.seconds)
            }
        }
            .flowOn(dispatcher)
    }
}
