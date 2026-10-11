package com.tgp.mockexam.feature.exam

import androidx.compose.runtime.Immutable
import com.tgp.mockexam.core.domain.engine.ExamResult
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.ExamSession
import com.tgp.mockexam.core.model.ExamStatus
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.Question

@Immutable
data class ExamUiState(
    val paperId: String,
    val loading: Boolean = true,
    val error: String? = null,
    val paper: Paper? = null,
    val session: ExamSession? = null,
    val language: ContentLanguage = ContentLanguage.ENGLISH,
    val remainingMs: Long = 0L,
    val showPalette: Boolean = false,
    val showSubmitDialog: Boolean = false,
    val showExitDialog: Boolean = false,
    val result: ExamResult? = null,
) {
    val isRunning: Boolean get() = session?.status == ExamStatus.IN_PROGRESS
    val isSubmitted: Boolean get() = session?.status == ExamStatus.SUBMITTED
    val currentQuestion: Question?
        get() {
            val target = session?.currentQuestionNumber ?: return null
            return paper?.questions?.firstOrNull { it.number == target }
        }
}