package com.tgp.mockexam.core.domain.repository

import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.ExamSession
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.PaperSummary
import kotlinx.coroutines.flow.Flow

interface PaperRepository {
    fun observePaper(paperId: String): Flow<Paper?>
    suspend fun getPaper(paperId: String): Paper?
    suspend fun listPapers(): List<PaperSummary>
}

interface ExamSessionRepository {
    fun observeSession(paperId: String): Flow<ExamSession?>
    suspend fun getSession(paperId: String): ExamSession?
    suspend fun startSession(paper: Paper, language: ContentLanguage)
    suspend fun saveAnswer(paperId: String, answer: AttemptAnswer)
    suspend fun setCurrentQuestion(paperId: String, questionNumber: Int)
    suspend fun submitSession(paperId: String)
    suspend fun resetSession(paperId: String)
}