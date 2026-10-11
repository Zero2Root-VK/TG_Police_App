package com.tgp.mockexam.core.data.repository

import android.os.SystemClock
import com.tgp.mockexam.core.database.dao.ExamSessionDao
import com.tgp.mockexam.core.data.mapper.toEntity
import com.tgp.mockexam.core.data.mapper.toModel
import com.tgp.mockexam.core.domain.repository.ExamSessionRepository
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.ExamSession
import com.tgp.mockexam.core.model.ExamStatus
import com.tgp.mockexam.core.model.Paper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RoomExamSessionRepository @Inject constructor(
    private val dao: ExamSessionDao,
) : ExamSessionRepository {

    override fun observeSession(paperId: String): Flow<ExamSession?> =
        combine(dao.observeSession(paperId), dao.observeAnswers(paperId)) { session, answers ->
            session?.toModel(answers.associate { it.questionNumber to it.toModel() })
        }

    override suspend fun getSession(paperId: String): ExamSession? {
        val session = dao.getSession(paperId) ?: return null
        return session.toModel(dao.getAnswers(paperId).associate { it.questionNumber to it.toModel() })
    }

    override suspend fun startSession(paper: Paper, language: ContentLanguage) {
        val existing = dao.getSession(paper.id)
        if (existing != null && existing.status == ExamStatus.IN_PROGRESS.name) return

        dao.deleteSession(paper.id)
        dao.insertSession(
            com.tgp.mockexam.core.data.mapper.entityOf(
                paperId = paper.id,
                status = ExamStatus.IN_PROGRESS.name,
                startedAtEpochMs = System.currentTimeMillis(),
                startedAtRealtimeMs = SystemClock.elapsedRealtime(),
                durationMins = paper.durationMins,
                pausedAccumulatedMs = 0L,
                currentQuestionNumber = 1,
                submittedAtEpochMs = null,
                language = language.name,
            ),
        )
    }

    override suspend fun saveAnswer(paperId: String, answer: AttemptAnswer) {
        dao.upsertAnswers(listOf(answer.toEntity(paperId)))
    }

    override suspend fun setCurrentQuestion(paperId: String, questionNumber: Int) {
        dao.updateCurrentQuestion(paperId, questionNumber)
    }

    override suspend fun submitSession(paperId: String) {
        dao.submit(paperId, ExamStatus.SUBMITTED.name, System.currentTimeMillis())
    }

    override suspend fun resetSession(paperId: String) {
        dao.deleteSession(paperId)
    }
}