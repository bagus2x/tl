package bagus2x.tl.data

import bagus2x.tl.data.local.MessageLocalDataSource
import bagus2x.tl.data.remote.MessageRemoteDataSource
import bagus2x.tl.domain.model.Message
import bagus2x.tl.domain.repository.MessageRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource,
    private val localDataSource: MessageLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MessageRepository {

    override suspend fun send(receiverId: Long, description: String, file: File?) {
        withContext(dispatcher) {
            remoteDataSource.sendMessage(receiverId, description, file)
        }
    }

    override fun getMessages(receiverId: Long): Flow<List<Message>> {
        return flow {
            coroutineScope {
                launch {
                    val res = remoteDataSource.getMessages(receiverId)
                    localDataSource.save(res)
                }
                launch {
                    remoteDataSource.observeNewMessage(receiverId).collect {
                        localDataSource.save(it)
                    }
                }
                val chats = localDataSource
                    .getMessages(receiverId)
                    .map { it.map { dto -> dto.asModel() } }
                emitAll(chats)
            }
        }
            .flowOn(dispatcher)
    }

    override fun getLastMessages(): Flow<List<Message>> {
        return flow {
            val res = remoteDataSource.getLastMessage().map { it.asModel() }
            emit(res)
        }
    }
}
