package com.tgp.mockexam.bootstrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface BootstrapState {
    data object Loading : BootstrapState
    data object Onboarding : BootstrapState
    data object Main : BootstrapState
}

@HiltViewModel
class BootstrapViewModel @Inject constructor(
    private val preferences: UserPreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<BootstrapState>(BootstrapState.Loading)
    val state: StateFlow<BootstrapState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val completed = preferences.onboardingCompleted.first()
            _state.value = if (completed) BootstrapState.Main else BootstrapState.Onboarding
        }
    }
}
