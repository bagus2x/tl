package bagus2x.tl.data

import bagus2x.tl.TestUtils
import bagus2x.tl.data.dto.AnnouncementDTO
import bagus2x.tl.domain.model.Announcement
import bagus2x.tl.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.io.File

val Announcements = LongArray(10) { it + 1L }
    .map {
        AnnouncementDTO(
            id = it,
            author = Users.random(),
            description = TestUtils.getRandomWords(200),
            file = "https://i.picsum.photos/id/804/200/300.jpg",
            createdAt = System.currentTimeMillis() - it * 1000
        )
    }

class FakeAnnouncementRepository : AnnouncementRepository {
    private val announcements = MutableStateFlow(Announcements)

    override suspend fun save(description: String, file: File?) {
        announcements.update { announcements ->
            val new = AnnouncementDTO(
                id = announcements.last().id + 1,
                author = Users.random(),
                description = description,
                file = file?.path,
                createdAt = System.currentTimeMillis()
            )
            (announcements + new).sortedByDescending { it.createdAt }
        }
    }

    override fun getAnnouncements(query: String, authorId: Long?): Flow<List<Announcement>> {
        return announcements.map { it.map { it.asModel() } }
    }
}
