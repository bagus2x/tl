package bagus2x.tl.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bagus2x.tl.data.dto.AnnouncementDTO
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AnnouncementLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    abstract suspend fun save(announcement: AnnouncementDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(announcements: List<AnnouncementDTO>)

    @Query("""
        SELECT * FROM announcement 
        WHERE LOWER(description) 
        LIKE '%' || :query || '%' ORDER BY created_at DESC
    """)
    abstract fun getAnnouncements(
        query: String
    ): Flow<List<AnnouncementDTO>>

    @Query("""
        SELECT * FROM announcement 
        WHERE LOWER(description) 
        LIKE '%' || :query || '%' AND author_id = :authorId 
        ORDER BY created_at DESC
    """)
    abstract fun getAnnouncements(
        query: String,
        authorId: Long
    ): Flow<List<AnnouncementDTO>>
}
