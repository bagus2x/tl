package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Message
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MessageRepository {

    suspend fun send(receiverId: Long, description: String, file: File?)

    fun getMessages(receiverId: Long): Flow<List<Message>>

    fun getLastMessages(): Flow<List<Message>>
}
