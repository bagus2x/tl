package bagus2x.tl.presentation.chat.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.GetLastMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    getLastMessagesUseCase: GetLastMessagesUseCase
) : ViewModel() {
    val lastMessages = getLastMessagesUseCase()
        .catch { e ->
            _snackbar.emit(e.message ?: "")
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _snackbar = MutableStateFlow("")
    val snackbar = _snackbar.asStateFlow()

    fun snackbarConsumed() {
        _snackbar.update { "" }
    }
}
