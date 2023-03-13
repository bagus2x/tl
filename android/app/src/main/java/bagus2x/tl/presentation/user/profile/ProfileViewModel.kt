package bagus2x.tl.presentation.user.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserUseCase: GetUserUseCase,
    getTestimoniesUseCase: GetTestimoniesUseCase,
    getAnnouncementsUseCase: GetAnnouncementsUseCase,
    private val friendUseCase: FriendUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val userId = savedStateHandle
        .getStateFlow<Long?>("user_id", null)
    val user = userId
        .flatMapLatest { userId ->
            getUserUseCase(userId)
        }
        .catch { e ->
            _snackbar.emit(e.message ?: "")
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val testimonies = user
        .filterNotNull()
        .flatMapLatest { user ->
            getTestimoniesUseCase(user.id)
        }
        .catch { e ->
            _snackbar.emit(e.message ?: "")
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val announcements = user
        .filterNotNull()
        .flatMapLatest { user ->
            getAnnouncementsUseCase(authorId = user.id)
        }
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

    fun friendship() {
        viewModelScope.launch {
            val userId = userId.value ?: return@launch
            try {
                friendUseCase(userId)
            } catch (e: Exception) {
                _snackbar.emit(e.message ?: "")
                Timber.e(e)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                signOutUseCase()
            }catch (e: Exception) {
                _snackbar.emit(e.message ?: "")
                Timber.e(e)
            }
        }
    }
}
