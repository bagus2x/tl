package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.AnnouncementRepository
import java.io.File

class AddAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
) {

    suspend operator fun invoke(description: String, file: File?) {
        if (file != null) {
            require(file.length() < (2 * 1024 * 1024)) {
                "File tidak boleh lebih dari 2 MB"
            }
        }
        require(description.length in 1..500) {
            "Deskripsi wajib diisi dan kurang dari 500 karakter"
        }
        announcementRepository.save(description, file)
    }
}
