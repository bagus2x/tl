package bagus2x.tl.presentation.invitation.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.CreateInvitationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InvitationFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createInvitationUseCase: CreateInvitationUseCase
) : ViewModel() {
    private val userId = requireNotNull(savedStateHandle.get<Long>("user_id"))
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _file = MutableStateFlow<File?>(null)
    val file = _file.asStateFlow()
    private val _submitted = MutableStateFlow(false)
    val sent = _submitted.asStateFlow()
    private val _snackbar = MutableStateFlow("")
    val snackbar = _snackbar.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun setDescription(description: String) {
        _description.update { description }
    }

    fun setFile(file: File) {
        _file.update { file }
    }

    fun send() {
        viewModelScope.launch {
            _loading.update { true }
            try {
                createInvitationUseCase(
                    inviteeId = userId,
                    description = _description.value,
                    file = _file.value
                )
                _submitted.update { true }
            } catch (e: Exception) {
                _snackbar.update { e.message ?: "" }
                Timber.e(e)
            }
            _loading.update { false }
        }
    }
}
