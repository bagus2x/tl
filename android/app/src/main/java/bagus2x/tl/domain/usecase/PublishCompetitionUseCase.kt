package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.CompetitionRepository
import java.io.File
import java.time.LocalDateTime

class PublishCompetitionUseCase(
    private val competitionRepository: CompetitionRepository
) {
    suspend operator fun invoke(
        poster: File,
        title: String,
        description: String,
        theme: String,
        city: String,
        country: String,
        deadline: LocalDateTime,
        minimumFee: Long,
        maximumFee: Long,
        category: String,
        organizer: String,
        organizerName: String,
        urlLink: String
    ) {
        require(poster.length() < (2 * 1024 * 1024)) {
            "File poster haru kurang dari 2 MB"
        }
        require(title.length in 5..256) {
            "Judul wajib diisi dengan lebih dari 5 karakter dan kurang dari 256 karakter"
        }
        require(description.length in 1..5000) {
            "Deskripsi wajib diisi"
        }
        require(theme.length in 1..500) {
            "Tema wajib diisi"
        }
        require(city.length in 1..500) {
            "Kota wajib diisi"
        }
        require(country.length in 1..500) {
            "Negara"
        }
        require(deadline > LocalDateTime.now()) {
            "Deadline tidak valid "
        }
        require(minimumFee >= 0) {
            "Biaya minimum tidak boleh negatif"
        }
        if (maximumFee != 0L) {
            require(maximumFee >= minimumFee) {
                "Biaya maximum harus lebih dari biaya minimum"
            }
        }
        require(category.length in 1..500) {
            "Kategori wajib diisi"
        }
        require(organizer.length in 1..500) {
            "Penyelenggara wajib diisi"
        }
        require(organizerName.length in 1..500) {
            "Nama penyelenggara wajib diisi"
        }
        require(urlLink.length in 1..500) {
            "URL Link wajib diisi"
        }

        competitionRepository.save(
            poster,
            title,
            description,
            theme,
            city,
            country,
            deadline,
            minimumFee,
            maximumFee,
            category,
            organizer,
            organizerName,
            urlLink
        )
    }
}
