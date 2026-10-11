package com.tgp.mockexam.feature.currentaffairs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.domain.repository.CurrentAffairsRepository
import com.tgp.mockexam.core.model.CurrentAffairsPack
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CurrentAffairsViewModel @Inject constructor(
    private val repository: CurrentAffairsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrentAffairsUiState())
    val uiState: StateFlow<CurrentAffairsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLatestPacks().collect { packs ->
                _uiState.update { it.copy(loading = false, packs = packs, error = null) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(loading = true, error = null) }
                repository.refresh()
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message ?: "Failed to load") }
            }
        }
    }
}

data class CurrentAffairsUiState(
    val loading: Boolean = true,
    val packs: List<CurrentAffairsPack> = emptyList(),
    val error: String? = null,
)
