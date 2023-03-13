package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.UserRepository
import java.io.File

class EditProfileUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        photo: File?,
        university: String,
        faculty: String,
        department: String,
        studyProgram: String,
        stream: String,
        batch: Int?,
        gender: String,
        age: Int?,
        bio: String,
        skills: List<String>,
        achievements: List<String>,
        certifications: List<String>,
        invitable: Boolean,
        location: String
    ) {
        if (photo != null) {
            require(photo.length() < (2 * 1024 * 1024)) {
                "Foto wajib kurang dari 2 MB"
            }
        }
        require(name.length in 5..64) {
            "Nama wajib diisi dengan lebih dari 5 karakter dan kurang dari 64 karakter"
        }

        userRepository.update(
            name = name,
            photo = photo,
            university = university,
            department = department,
            faculty = faculty,
            studyProgram = studyProgram,
            stream = stream,
            batch = batch,
            gender = gender,
            age = age,
            bio = bio,
            skills = skills,
            achievements = achievements,
            certifications = certifications,
            invitable = invitable,
            location = location
        )
    }
}
