package bagus2x.tl.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bagus2x.tl.data.dto.MessageDTO
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MessageLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(message: MessageDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(messages: List<MessageDTO>)

    @Query("SELECT * FROM message WHERE receiver_id = :receiverId OR sender_id = :receiverId ORDER BY created_at DESC")
    abstract fun getMessages(receiverId: Long): Flow<List<MessageDTO>>
}
