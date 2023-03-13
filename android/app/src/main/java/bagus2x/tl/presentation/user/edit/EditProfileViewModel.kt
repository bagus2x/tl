package bagus2x.tl.presentation.user.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.EditProfileUseCase
import bagus2x.tl.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val savedState: SavedStateHandle,
    private val editProfileUseCase: EditProfileUseCase,
) : ViewModel() {
    val user = getUserUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val snackbar = savedState.getStateFlow("snackbar", "")
    val photo = savedState.getStateFlow<File?>("photo", null)
    val name = savedState.getStateFlow("name", "")
    val bio = savedState.getStateFlow("bio", "")
    val invitable = savedState.getStateFlow("invitable", false)
    val university = savedState.getStateFlow("university", "")
    val faculty = savedState.getStateFlow("faculty", "")
    val batch = savedState.getStateFlow<Int?>("batch", null)
    val department = savedState.getStateFlow("department", "")
    val studyProgram = savedState.getStateFlow("studyProgram", "")
    val stream = savedState.getStateFlow("stream", "")
    val gender = savedState.getStateFlow("gender", "")
    val age = savedState.getStateFlow<Int?>("age", null)
    val location = savedState.getStateFlow("location", "")
    val skills = savedState.getStateFlow("skills", "")
    val certifications = savedState.getStateFlow("certifications", "")
    val achievements = savedState.getStateFlow("achievements", "")
    val loading = savedState.getStateFlow("loading", false)
    val saved = savedState.getStateFlow("saved", false)

    init {
        viewModelScope.launch {
            user.filterNotNull().collectLatest { user ->
                savedState["name"] = user.name
                savedState["bio"] = user.bio ?: ""
                savedState["invitable"] = user.invitable
                savedState["university"] = user.university ?: ""
                savedState["faculty"] = user.faculty ?: ""
                savedState["studyProgram"] = user.studyProgram ?: ""
                savedState["batch"] = user.batch
                savedState["department"] = user.department ?: ""
                savedState["stream"] = user.stream ?: ""
                savedState["gender"] = user.gender ?: ""
                savedState["age"] = user.age
                savedState["location"] = user.location ?: ""
                savedState["skills"] = user.skills
                    .joinToString(", ")
                savedState["certifications"] = user.certifications
                    .joinToString(", ")
                savedState["achievements"] = user.achievements
                    .joinToString(", ")
            }
        }
    }

    fun setName(name: String) {
        savedState["name"] = name
    }

    fun setPhoto(file: File) {
        savedState["photo"] = file
    }

    fun setUniversity(university: String) {
        savedState["university"] = university
    }

    fun setFaculty(faculty: String) {
        savedState["faculty"] = faculty
    }

    fun setDepartment(department: String) {
        savedState["department"] = department
    }

    fun setStudyProgram(studyProgram: String) {
        savedState["studyProgram"] = studyProgram
    }

    fun setStream(stream: String) {
        savedState["stream"] = stream
    }

    fun setBatch(batch: Int?) {
        savedState["batch"] = batch
    }

    fun setGender(gender: String) {
        savedState["gender"] = gender
    }

    fun setAge(age: Int?) {
        savedState["age"] = age
    }

    fun setBio(bio: String) {
        savedState["bio"] = bio
    }

    fun setInvitable(invitable: Boolean) {
        savedState["invitable"] = invitable
    }

    fun setLocation(location: String) {
        savedState["location"] = location
    }

    fun setSkills(skills: String) {
        savedState["skills"] = skills
    }

    fun setCertifications(certifications: String) {
        savedState["certifications"] = certifications
    }

    fun setAchievements(achievements: String) {
        savedState["achievements"] = achievements
    }


    fun save() {
        viewModelScope.launch {
            savedState["loading"] = true
            try {
                editProfileUseCase(
                    name = name.value,
                    photo = photo.value,
                    university = university.value,
                    department = department.value,
                    studyProgram = studyProgram.value,
                    stream = stream.value,
                    batch = batch.value,
                    faculty = faculty.value,
                    gender = gender.value,
                    age = age.value,
                    bio = bio.value,
                    skills = skills.value.toList(),
                    achievements = achievements.value.toList(),
                    certifications = certifications.value.toList(),
                    invitable = invitable.value,
                    location = location.value
                )
                savedState["saved"] = true
            } catch (e: Exception) {
                savedState["snackbar"] = e.message ?: ""
                Timber.e(e)
            }
            savedState["loading"] = false
        }
    }
}

private fun String.toList(): List<String> {
    return split(",")
        .map {
            it
                .replace("\n", "")
                .replace("\\s+", " ")
                .trim()
        }
}
