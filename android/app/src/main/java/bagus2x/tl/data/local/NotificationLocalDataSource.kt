package bagus2x.tl.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bagus2x.tl.data.dto.NotificationDTO
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotificationLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(notifications: List<NotificationDTO>)

    @Query("SELECT * FROM notification ORDER BY created_at DESC")
    abstract fun getNotifications(): Flow<List<NotificationDTO>>
}
