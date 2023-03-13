package bagus2x.tl.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bagus2x.tl.data.dto.TestimonyDTO
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TestimonyLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(testimony: TestimonyDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(testimonies: List<TestimonyDTO>)

    @Query("SELECT * FROM testimony WHERE receiver_id = :receiverId ORDER BY created_at DESC")
    abstract fun getTestimonies(receiverId: Long): Flow<List<TestimonyDTO>>
}
