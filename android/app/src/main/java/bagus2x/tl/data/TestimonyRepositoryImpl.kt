package bagus2x.tl.data

import bagus2x.tl.data.local.TestimonyLocalDataSource
import bagus2x.tl.data.remote.TestimonyRemoteDataSource
import bagus2x.tl.data.utils.networkBoundResource
import bagus2x.tl.domain.model.Testimony
import bagus2x.tl.domain.repository.TestimonyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TestimonyRepositoryImpl(
    private val remoteDataSource: TestimonyRemoteDataSource,
    private val localDataSource: TestimonyLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : TestimonyRepository {

    override suspend fun save(receiverId: Long, description: String, rating: Float) {
        withContext(dispatcher) {
            val res = remoteDataSource.save(receiverId, description, rating)
            localDataSource.save(res)
        }
    }

    override fun getTestimonies(receiverId: Long): Flow<List<Testimony>> {
        return networkBoundResource(
            local = {
                localDataSource.getTestimonies(receiverId)
            },
            remote = {
                remoteDataSource.getTestimonies(receiverId)
            },
            update = { res ->
                localDataSource.save(res)
            }
        )
            .map { dto -> dto.map { it.asModel() } }
            .flowOn(dispatcher)
    }
}
