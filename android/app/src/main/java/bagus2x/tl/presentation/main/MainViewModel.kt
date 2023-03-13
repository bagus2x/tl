package bagus2x.tl.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.GetAuthUseCase
import bagus2x.tl.domain.usecase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel @Inject constructor(
    getAuthUseCase: GetAuthUseCase,
    getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {
    val auth = getAuthUseCase()
        .catch { e ->
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val unreadNotifications = auth
        .flatMapLatest {
            if (it == null) emptyFlow()
            else getNotificationsUseCase.unread
        }
        .catch { e ->
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )
}
