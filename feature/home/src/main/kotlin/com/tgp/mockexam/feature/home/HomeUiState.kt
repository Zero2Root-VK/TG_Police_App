package com.tgp.mockexam.feature.home

import androidx.compose.runtime.Immutable
import com.tgp.mockexam.core.model.PaperSummary
import com.tgp.mockexam.core.model.Track

@Immutable
data class HomeUiState(
    val loading: Boolean = true,
    val papers: List<PaperSummary> = emptyList(),
    val error: String? = null,
) {
    val totalPapers: Int get() = papers.size
    val totalQuestions: Int get() = papers.sumOf { it.totalQuestions }

    fun papersFor(track: Track): Int = papers.count { it.track == track }
    fun questionsFor(track: Track): Int = papers.filter { it.track == track }.sumOf { it.totalQuestions }
}
