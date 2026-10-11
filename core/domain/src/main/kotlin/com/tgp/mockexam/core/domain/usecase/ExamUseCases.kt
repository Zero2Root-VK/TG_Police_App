package com.tgp.mockexam.core.domain.usecase

import com.tgp.mockexam.core.domain.repository.ExamSessionRepository
import com.tgp.mockexam.core.domain.repository.PaperRepository
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.ExamSession
import com.tgp.mockexam.core.model.Paper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class ExamObservation(
    val paper: Paper?,
    val session: ExamSession?,
)

class ObserveExamRunUseCase @Inject constructor(
    private val paperRepository: PaperRepository,
    private val examSessionRepository: ExamSessionRepository,
) {
    operator fun invoke(paperId: String): Flow<ExamObservation> =
        paperRepository.observePaper(paperId)
            .combine(examSessionRepository.observeSession(paperId)) { paper, session ->
                ExamObservation(paper, session)
            }
}

class StartExamUseCase @Inject constructor(
    private val paperRepository: PaperRepository,
    private val examSessionRepository: ExamSessionRepository,
) {
    suspend operator fun invoke(paperId: String) {
        val paper = paperRepository.getPaper(paperId) ?: return
        examSessionRepository.startSession(paper, com.tgp.mockexam.core.model.ContentLanguage.ENGLISH)
    }
}

class SaveAnswerUseCase @Inject constructor(
    private val examSessionRepository: ExamSessionRepository,
) {
    suspend operator fun invoke(paperId: String, answer: AttemptAnswer) {
        examSessionRepository.saveAnswer(paperId, answer)
    }
}

class SetCurrentQuestionUseCase @Inject constructor(
    private val examSessionRepository: ExamSessionRepository,
) {
    suspend operator fun invoke(paperId: String, questionNumber: Int) {
        examSessionRepository.setCurrentQuestion(paperId, questionNumber)
    }
}

class SubmitExamUseCase @Inject constructor(
    private val examSessionRepository: ExamSessionRepository,
) {
    suspend operator fun invoke(paperId: String) {
        examSessionRepository.submitSession(paperId)
    }
}

class ResetExamUseCase @Inject constructor(
    private val examSessionRepository: ExamSessionRepository,
) {
    suspend operator fun invoke(paperId: String) {
        examSessionRepository.resetSession(paperId)
    }
}