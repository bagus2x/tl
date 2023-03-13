package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.model.Announcement
import bagus2x.tl.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow

class GetAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository
) {

    operator fun invoke(
        query: String = "",
        authorId: Long? = null
    ): Flow<List<Announcement>> {
        return announcementRepository.getAnnouncements(query, authorId)
    }
}
