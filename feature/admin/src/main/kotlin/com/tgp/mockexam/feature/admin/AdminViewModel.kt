package com.tgp.mockexam.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.domain.repository.PaperRepository
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.Question
import com.tgp.mockexam.core.network.AdminWriteClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val paperRepository: PaperRepository,
    private val preferences: UserPreferencesRepository,
    private val writeClient: AdminWriteClient,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        refresh()
        viewModelScope.launch {
            preferences.adminApiKey.collect { key ->
                _uiState.update { it.copy(apiKey = key) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(loading = true) }
                val summaries = paperRepository.listPapers()
                val papers = mutableListOf<Paper>()
                for (sum in summaries) {
                    val p = paperRepository.getPaper(sum.id)
                    if (p != null) papers.add(p)
                }
                _uiState.update { it.copy(loading = false, papers = papers, error = null) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message) }
            }
        }
    }

    fun selectPaper(paperId: String?) {
        _uiState.update { it.copy(selectedPaperId = paperId, editingQuestion = null) }
    }

    fun setEditingQuestion(q: Question?) {
        _uiState.update { it.copy(editingQuestion = q) }
    }

    fun setShowApiKey(show: Boolean) {
        _uiState.update { it.copy(showApiKeyDialog = show) }
    }

    fun setShowCreatePaper(show: Boolean) {
        _uiState.update { it.copy(showCreatePaperDialog = show) }
    }

    fun setShowCreateQuestion(show: Boolean) {
        _uiState.update { it.copy(showCreateQuestionDialog = show) }
    }

    fun setShowImport(show: Boolean) {
        _uiState.update { it.copy(showImportDialog = show) }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            preferences.setAdminApiKey(key)
            _uiState.update { it.copy(showApiKeyDialog = false) }
        }
    }
}

data class AdminUiState(
    val loading: Boolean = true,
    val papers: List<Paper> = emptyList(),
    val selectedPaperId: String? = null,
    val editingQuestion: Question? = null,
    val error: String? = null,
    val apiKey: String = "",
    val showApiKeyDialog: Boolean = false,
    val showCreatePaperDialog: Boolean = false,
    val showCreateQuestionDialog: Boolean = false,
    val showImportDialog: Boolean = false,
) {
    val selectedPaper: Paper? get() = papers.find { it.id == selectedPaperId }
    val canWrite: Boolean get() = apiKey.isNotBlank()
}
