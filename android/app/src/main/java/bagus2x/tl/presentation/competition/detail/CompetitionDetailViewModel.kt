package bagus2x.tl.presentation.competition.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.FavCompetitionUseCase
import bagus2x.tl.domain.usecase.GetCompetitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CompetitionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCompetitionUseCase: GetCompetitionUseCase,
    private val favCompetitionUseCase: FavCompetitionUseCase
) : ViewModel() {
    private val competitionId = requireNotNull(savedStateHandle.get<Long>("competition_id"))
    val competition = getCompetitionUseCase(competitionId)
        .catch { e ->
            _snackbar.emit(e.message ?: "")
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private val _snackbar = MutableStateFlow("")
    val snackbar = _snackbar.asStateFlow()

    fun favorite() {
        viewModelScope.launch {
            try {
                favCompetitionUseCase(competitionId)
            }catch (e: Exception) {
                _snackbar.update { e.message ?: "" }
                Timber.e(e)
            }
        }
    }
}
