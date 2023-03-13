package bagus2x.tl.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAnnouncementsUseCase: GetAnnouncementsUseCase,
    getCompetitionsUseCase: GetCompetitionsUseCase,
    getUsersUseCase: GetUsersUseCase,
    getMessagesUseCase: GetMessagesUseCase,
    getLastMessagesUseCase: GetLastMessagesUseCase
) : ViewModel() {
    private val type = requireNotNull(savedStateHandle.get<String>("type"))
    val userId = savedStateHandle.get<String>("user_id")?.toLong()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val announcements = _query
        .filter { type == "announcements" }
        .flatMapLatest { getAnnouncementsUseCase(it) }
        .catch { Timber.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val users = _query
        .filter { type == "users" }
        .flatMapLatest { getUsersUseCase(it) }
        .catch { Timber.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val competitions = _query
        .filter { type == "competitions" }
        .flatMapLatest { getCompetitionsUseCase(it) }
        .catch { Timber.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val messages = _query
        .filter { type == "messages" }
        .flatMapLatest { getMessagesUseCase(it, userId ?: 0) }
        .catch { Timber.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val lastMessages = _query
        .filter { type == "last_messages" }
        .flatMapLatest { if (it.isBlank()) emptyFlow() else getLastMessagesUseCase(it) }
        .catch { Timber.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun setQuery(query: String) {
        _query.update { query }
    }
}
