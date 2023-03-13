package bagus2x.tl.presentation.invitation.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.RespondInvitationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ResponseFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val respondInvitationUseCase: RespondInvitationUseCase
) : ViewModel() {
    private val invitationId = requireNotNull(savedStateHandle.get<Long>("invitation_id"))
    private val accepted = requireNotNull(savedStateHandle.get<Boolean>("accepted"))
    private val _submitted = MutableStateFlow(false)
    val submitted = _submitted.asStateFlow()
    private val _response = MutableStateFlow("")
    val response = _response.asStateFlow()
    private val _snackbar = MutableStateFlow("")
    val snackbar = _snackbar.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun setResponse(description: String) {
        _response.update { description }
    }

    fun send() {
        viewModelScope.launch {
            _loading.update { true }
            try {
                respondInvitationUseCase(
                    invitationId = invitationId,
                    response = _response.value,
                    accepted = accepted
                )
                _submitted.update { true }
            } catch (e: Exception) {
                _snackbar.update { e.message ?: "" }
                Timber.e(e)
            }
            _loading.update { false}
        }
    }
}
