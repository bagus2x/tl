package bagus2x.tl.presentation.competition.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.FavCompetitionUseCase
import bagus2x.tl.domain.usecase.GetCompetitionsUseCase
import bagus2x.tl.presentation.common.Utils.asParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CompetitionListViewModel @Inject constructor(
    getCompetitionsUseCase: GetCompetitionsUseCase,
    private val savedState: SavedStateHandle,
    private val favCompetitionUseCase: FavCompetitionUseCase
) : ViewModel() {
    val filter = savedState.getStateFlow("filter", emptyMap<String, Map<String, Boolean>>())
    val order = savedState.getStateFlow("order", "")
    val competitions = combine(filter, order) { filter, order -> order to filter }
        .flatMapLatest { (order, filter) ->
            getCompetitionsUseCase(order = order, filter = filter.asParams())
        }
        .catch { e ->
            savedState["snackbar"] = e.message ?: ""
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val snackbar = savedState.getStateFlow("snackbar", "")

    fun snackbarConsumed() {
        savedState["snackbar"] = ""
    }

    fun setFilter(filter: Map<String, Map<String, Boolean>>) {
        savedState["filter"] = filter
    }

    fun setOrder(order: String) {
        savedState["order"] = order
    }

    fun toggleFavorite(competitionId: Long) {
        viewModelScope.launch {
            try {
                favCompetitionUseCase(competitionId)
            } catch (e: Exception) {
                savedState["snackbar"] = e.message ?: ""
                Timber.e(e)
            }
        }
    }
}
