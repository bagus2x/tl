package bagus2x.tl.presentation.invitation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.model.Invitation
import bagus2x.tl.domain.usecase.GetInvitationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationDetailViewModel @Inject constructor(
    savedState: SavedStateHandle,
    getInvitationUseCase: GetInvitationUseCase
) : ViewModel() {
    private val invitationId = requireNotNull(savedState.get<Long>("invitation_id"))
    private val _invitation = MutableStateFlow<Invitation?>(null)
    val invitation = _invitation.asStateFlow()

    init {
        viewModelScope.launch {
            val invitation = getInvitationUseCase(invitationId)
            _invitation.update { invitation }
        }
    }
}
