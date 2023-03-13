package bagus2x.tl.presentation.auth.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    val name = savedState.getStateFlow("name", "")
    val email = savedState.getStateFlow("email", "")
    val password = savedState.getStateFlow("password", "")
    val loading = savedState.getStateFlow("loading", false)
    val snackbar = savedState.getStateFlow("snackbar", "")

    fun snackbarConsumed() {
        savedState["snackbar"] = ""
    }

    fun setEmail(email: String) {
        savedState["email"] = email
    }

    fun setPassword(password: String) {
        savedState["password"] = password
    }

    fun signIn() {
        viewModelScope.launch {
            savedState["loading"] = true
            try {
                signInUseCase(
                    email = email.value,
                    password = password.value
                )
            } catch (e: Exception) {
                savedState["snackbar"] = e.message
                Timber.e(e)
            }
            savedState["loading"] = false
        }
    }
}
