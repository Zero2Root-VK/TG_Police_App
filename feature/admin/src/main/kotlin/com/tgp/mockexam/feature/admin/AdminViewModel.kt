package com.tgp.mockexam.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.domain.repository.PaperRepository
import com.tgp.mockexam.core.model.Paper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val paperRepository: PaperRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(loading = true) }
                val papers = paperRepository.listPapers().map { 
                    // map summary to minimal Paper? easier: just load full papers or show summaries
                    paperRepository.getPaper(it.id)
                }.filterNotNull()
                _uiState.update { it.copy(loading = false, papers = papers) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message) }
            }
        }
    }

    fun selectPaper(paperId: String?) {
        _uiState.update { it.copy(selectedPaperId = paperId) }
    }
}

data class AdminUiState(
    val loading: Boolean = true,
    val papers: List<Paper> = emptyList(),
    val selectedPaperId: String? = null,
    val error: String? = null,
) {
    val selectedPaper: Paper? get() = papers.find { it.id == selectedPaperId }
}
