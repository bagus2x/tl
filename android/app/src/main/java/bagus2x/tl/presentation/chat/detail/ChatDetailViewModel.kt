package bagus2x.tl.presentation.chat.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.GetMessagesUseCase
import bagus2x.tl.domain.usecase.GetUserUseCase
import bagus2x.tl.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedState: SavedStateHandle,
    getUserUseCase: GetUserUseCase,
    getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    private val userId = requireNotNull(savedState.get<Long>("user_id"))
    val user = getUserUseCase(userId)
        .catch { e ->
            Timber.e(e)
            _snackbar.emit(e.message ?: "")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
    val messages = getMessagesUseCase(receiverId = userId)
        .catch { e ->
            Timber.e(e)
            _snackbar.emit(e.message ?: "")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    private val _newMessage = MutableStateFlow("")
    val newMessage = _newMessage.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _snackbar = MutableStateFlow("")
    val snackbar = _snackbar.asStateFlow()

    fun snackbarConsumed() {
        _snackbar.update { "" }
    }

    fun setNewMessage(message: String) {
        _newMessage.update { message }
    }

    fun send() {
        if (_newMessage.value.isBlank()) {
            return
        }

        viewModelScope.launch {
            _loading.update { true }
            try {
                sendMessageUseCase(
                    receiverId = userId,
                    description = newMessage.value,
                    file = null
                )
            } catch (e: Exception) {
                _snackbar.emit(e.message ?: "Try again later")
                Timber.e(e)
            }
            _loading.update { false }
            _newMessage.update { "" }
        }
    }
}
