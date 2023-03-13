package bagus2x.tl.presentation.testimony

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.CreateTestimonyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TestimonyFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createTestimonyUseCase: CreateTestimonyUseCase
) : ViewModel() {
    private val userId = requireNotNull(savedStateHandle.get<Long>("user_id"))

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _rating = MutableStateFlow(0F)
    val rating = _rating.asStateFlow()

    private val _snackbar = MutableStateFlow("")
    val snackbar = _snackbar.asStateFlow()

    private val _submitted = MutableStateFlow(false)
    val sent = _submitted.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun snackbarConsumed() {
        _snackbar.update { "" }
    }

    fun setDescription(description: String) {
        _description.update { description }
    }

    fun setRating(rating: Float) {
        _rating.update { rating }
    }

    fun send() {
        viewModelScope.launch {
            _loading.update { true }
            try {
                createTestimonyUseCase(
                    receiverId = userId,
                    description = _description.value,
                    rating = _rating.value
                )
                _submitted.update { true }
            } catch (e: Exception) {
                Timber.e(e)
            }
            _loading.update { false }
        }
    }
}
