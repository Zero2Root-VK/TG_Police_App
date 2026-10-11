package com.tgp.mockexam.core.model

/** Per-question state shown in the exam palette. Mirrors real exam conventions. */
enum class QuestionState {
    NOT_VISITED,
    NOT_ANSWERED,
    ANSWERED,
    MARKED_FOR_REVIEW,
    ANSWERED_AND_MARKED,
}

enum class ExamStatus {
    NOT_STARTED,
    IN_PROGRESS,
    SUBMITTED,
    SCORED,
}

data class AttemptAnswer(
    val questionNumber: Int,
    val selectedOptionIndex: Int?,
    val markedForReview: Boolean,
    val visited: Boolean,
)

data class ExamSession(
    val paperId: String,
    val status: ExamStatus,
    val startedAtEpochMs: Long,
    val startedAtRealtimeMs: Long,
    val durationMins: Int,
    val pausedAccumulatedMs: Long,
    val submittedAtEpochMs: Long?,
    val answers: Map<Int, AttemptAnswer>,
    val currentQuestionNumber: Int,
)
