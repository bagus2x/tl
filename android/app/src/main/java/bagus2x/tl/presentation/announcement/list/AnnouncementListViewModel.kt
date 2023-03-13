package bagus2x.tl.presentation.announcement.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.GetAnnouncementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnnouncementListViewModel @Inject constructor(
    getAnnouncementsUseCase: GetAnnouncementsUseCase
) : ViewModel() {
    val announcements = getAnnouncementsUseCase()
        .catch { e ->
            _snackbar.update { e.message ?: "" }
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
