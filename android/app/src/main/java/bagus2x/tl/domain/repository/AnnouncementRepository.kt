package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.Announcement
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AnnouncementRepository {

    suspend fun save(description: String, file: File?)

    fun getAnnouncements(query: String, authorId: Long?): Flow<List<Announcement>>
}
