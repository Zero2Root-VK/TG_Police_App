package com.tgp.mockexam.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository
import com.tgp.mockexam.core.model.ContentLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.uiLanguage.collect { language ->
                _uiState.update { it.copy(language = language) }
            }
        }
        viewModelScope.launch {
            preferences.selectedTrack.collect { track ->
                val choice = when (track) {
                    null -> ExamChoice.BOTH
                    else -> ExamChoice.entries.firstOrNull { it.track == track } ?: ExamChoice.BOTH
                }
                _uiState.update { it.copy(examChoice = choice) }
            }
        }
    }

    fun selectLanguage(language: ContentLanguage) {
        _uiState.update { it.copy(language = language) }
    }

    fun selectExam(choice: ExamChoice) {
        _uiState.update { it.copy(examChoice = choice) }
    }

    fun finish(onDone: () -> Unit) {
        if (_uiState.value.saving) return
        _uiState.update { it.copy(saving = true) }
        val state = _uiState.value
        viewModelScope.launch {
            preferences.setUiLanguage(state.language)
            preferences.setSelectedTrack(state.examChoice.track)
            preferences.setOnboardingCompleted(true)
            _uiState.update { it.copy(saving = false) }
            onDone()
        }
    }
}
