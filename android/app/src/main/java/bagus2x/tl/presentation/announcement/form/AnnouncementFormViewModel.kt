package bagus2x.tl.presentation.announcement.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.AddAnnouncementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AnnouncementFormViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val addAnnouncementUseCase: AddAnnouncementUseCase
) : ViewModel() {
    val description = savedState.getStateFlow("description", "")
    val file = savedState.getStateFlow<File?>("file", null)
    val loading = savedState.getStateFlow("loading", false)
    val snackbar = savedState.getStateFlow("snackbar", "")
    val submitted = savedState.getStateFlow("submitted", false)

    fun snackbarConsumed() {
        savedState["snackbar"] = ""
    }

    fun setDescription(description: String) {
        savedState["description"] = description
    }

    fun setFile(file: File?) {
        savedState["file"] = file
    }

    fun post() {
        viewModelScope.launch {
            savedState["loading"] = true
            try {
                addAnnouncementUseCase(
                    description = description.value,
                    file = file.value
                )
                savedState["submitted"] = true
            } catch (e: Exception) {
                savedState["snackbar"] = e.message
                Timber.e(e)
            }
            savedState["loading"] = false
        }
    }
}
