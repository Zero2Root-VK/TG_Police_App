package com.tgp.mockexam.feature.catalog

import androidx.compose.runtime.Immutable
import com.tgp.mockexam.core.model.PaperSummary

@Immutable
data class CatalogUiState(
    val loading: Boolean = true,
    val papers: List<PaperSummary> = emptyList(),
    val error: String? = null,
)
