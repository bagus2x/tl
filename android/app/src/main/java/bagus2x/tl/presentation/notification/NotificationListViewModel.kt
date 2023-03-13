package bagus2x.tl.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.FriendUseCase
import bagus2x.tl.domain.usecase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val friendUseCase: FriendUseCase
) : ViewModel() {
    val notifications = getNotificationsUseCase()
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

    fun respondFriendshipRequest(userId: Long, accepted: Boolean) {
        viewModelScope.launch {
            try {
                friendUseCase(userId, accepted)
            } catch (e: Exception) {
                _snackbar.emit(e.message ?: "")
                Timber.e(e)
            }
        }
    }
}
