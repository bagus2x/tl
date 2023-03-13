package bagus2x.tl.presentation.chat.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.GetAuthUseCase
import bagus2x.tl.domain.usecase.GetFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    getAuthUseCase: GetAuthUseCase,
    getFriendsUseCase: GetFriendsUseCase
) : ViewModel() {
    val users = getAuthUseCase()
        .filterNotNull()
        .map { auth ->
            getFriendsUseCase(userId = auth.userId)
        }
        .catch { e ->
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
}
