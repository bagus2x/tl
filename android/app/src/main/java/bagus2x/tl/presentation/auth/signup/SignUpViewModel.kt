package bagus2x.tl.presentation.auth.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.GetAuthUseCase
import bagus2x.tl.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    getAuthUseCase: GetAuthUseCase,
    private val savedState: SavedStateHandle,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    val auth = getAuthUseCase()
        .catch { e ->
            savedState["snackbar"] = e.message
            Timber.e(e)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
    val name = savedState.getStateFlow("name", "")
    val email = savedState.getStateFlow("email", "")
    val password = savedState.getStateFlow("password", "")
    val code = savedState.getStateFlow("code", "")
    val loading = savedState.getStateFlow("loading", false)
    val snackbar = savedState.getStateFlow("snackbar", "")

    fun snackbarConsumed() {
        savedState["snackbar"] = ""
    }

    fun setName(name: String) {
        savedState["name"] = name
    }

    fun setEmail(email: String) {
        savedState["email"] = email
    }

    fun setPassword(password: String) {
        savedState["password"] = password
    }

    fun setVerificationCode(code: String) {
        if (code.length <= 4) {
            savedState["code"] = code
        }
    }

    fun signUp() {
        viewModelScope.launch {
            savedState["loading"] = true
            try {
                signUpUseCase(
                    name = name.value,
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

    fun verify() {
        viewModelScope.launch {
            savedState["loading"] = true
            try {
                signUpUseCase.verify(code = code.value)
            } catch (e: Exception) {
                savedState["snackbar"] = e.message
                Timber.e(e)
            }
            savedState["loading"] = false
        }
    }
}
