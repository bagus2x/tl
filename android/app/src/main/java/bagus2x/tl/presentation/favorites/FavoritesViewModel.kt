package bagus2x.tl.presentation.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAuthUseCase: GetAuthUseCase,
    getFavUsersUseCase: GetFavUsersUseCase,
    getFavCompetitionsUseCase: GetFavCompetitionsUseCase,
    private val favUserUseCase: FavUserUseCase,
    private val favCompetitionsUseCase: FavCompetitionUseCase,
) : ViewModel() {
    private val userId = combine(
        flow = savedStateHandle.getStateFlow<String?>("user_id", null),
        flow2 = getAuthUseCase()
    ) { userId, auth ->
        userId?.toLongOrNull() ?: auth?.userId
    }
    val users = userId
        .filterNotNull()
        .map(getFavUsersUseCase::invoke)
        .catch { e->
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val competitions = userId
        .filterNotNull()
        .map(getFavCompetitionsUseCase::invoke)
        .catch { e->
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun favoriteUser(userId: Long) {
        viewModelScope.launch {
            try {
                favUserUseCase(userId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun favoriteCompetition(competitionId: Long) {
        viewModelScope.launch {
            try {
                favCompetitionsUseCase(competitionId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
