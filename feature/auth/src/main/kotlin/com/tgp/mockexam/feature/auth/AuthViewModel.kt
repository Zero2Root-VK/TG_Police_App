package com.tgp.mockexam.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.common.TgpResult
import com.tgp.mockexam.core.domain.repository.AuthRepository
import com.tgp.mockexam.core.domain.repository.ProfileRepository
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val preferences: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeUser().collect { user ->
                val role = try {
                    if (user != null) profileRepository.getRole(user.id) else "student"
                } catch (_: Throwable) {
                    "student"
                }
                _uiState.update { it.copy(user = user, role = role) }
            }
        }
        viewModelScope.launch {
            preferences.uiLanguage.collect { language ->
                _uiState.update { it.copy(language = language) }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }

    fun onEmailChange(value: String) = _uiState.update {
        it.copy(email = value, error = null, magicLinkSent = false)
    }

    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, error = null) }

    fun togglePasswordVisibility() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun toggleMode() = _uiState.update {
        it.copy(
            mode = if (it.mode == AuthMode.SIGN_IN) AuthMode.SIGN_UP else AuthMode.SIGN_IN,
            error = null,
            magicLinkSent = false,
        )
    }

    fun onGoogleSignIn() = _uiState.update {
        it.copy(error = null, browserUrl = authRepository.googleSignInUrl())
    }

    fun onBrowserLaunched() = _uiState.update { it.copy(browserUrl = null) }

    fun sendMagicLink() {
        val state = _uiState.value
        if (!state.canSendMagicLink) return
        _uiState.update { it.copy(loading = true, error = null, magicLinkSent = false) }
        viewModelScope.launch {
            when (val result = authRepository.sendMagicLink(state.email)) {
                is TgpResult.Success -> _uiState.update {
                    it.copy(loading = false, magicLinkSent = true)
                }

                is TgpResult.Error -> _uiState.update {
                    it.copy(loading = false, error = result.throwable.message ?: "Something went wrong")
                }
            }
        }
    }

    fun submit(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.canSubmit) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val result = when (state.mode) {
                AuthMode.SIGN_IN -> authRepository.signIn(state.email, state.password)
                AuthMode.SIGN_UP -> authRepository.signUp(state.name, state.email, state.password)
            }
            when (result) {
                is TgpResult.Success -> {
                    _uiState.update { it.copy(loading = false) }
                    onSuccess()
                }

                is TgpResult.Error -> _uiState.update {
                    it.copy(loading = false, error = result.throwable.message ?: "Something went wrong")
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }
}
