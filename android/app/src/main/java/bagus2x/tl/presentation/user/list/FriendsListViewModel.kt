package bagus2x.tl.presentation.user.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.usecase.FavUserUseCase
import bagus2x.tl.domain.usecase.GetFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFriendsUseCase: GetFriendsUseCase,
    private val favUserUseCase: FavUserUseCase
) : ViewModel() {
    private val userId = requireNotNull(savedStateHandle.get<Long>("user_id"))
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val friends = getFriendsUseCase(userId)
                _users.update { friends }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            try {
                favUserUseCase(userId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
