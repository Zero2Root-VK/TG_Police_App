package com.tgp.mockexam.core.data.mapper

import com.tgp.mockexam.core.database.model.AttemptAnswerEntity
import com.tgp.mockexam.core.database.model.ExamSessionEntity
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.ExamSession
import com.tgp.mockexam.core.model.ExamStatus

internal fun ExamSessionEntity.toModel(answers: Map<Int, AttemptAnswer>): ExamSession = ExamSession(
    paperId = paperId,
    status = runCatching { ExamStatus.valueOf(status) }.getOrDefault(ExamStatus.NOT_STARTED),
    startedAtEpochMs = startedAtEpochMs,
    startedAtRealtimeMs = startedAtRealtimeMs,
    durationMins = durationMins,
    pausedAccumulatedMs = pausedAccumulatedMs,
    submittedAtEpochMs = submittedAtEpochMs,
    answers = answers,
    currentQuestionNumber = currentQuestionNumber,
)

internal fun AttemptAnswerEntity.toModel(): AttemptAnswer = AttemptAnswer(
    questionNumber = questionNumber,
    selectedOptionIndex = selectedOptionIndex,
    markedForReview = markedForReview,
    visited = visited,
)

internal fun AttemptAnswer.toEntity(paperIdLocal: String): AttemptAnswerEntity = AttemptAnswerEntity(
    paperId = paperIdLocal,
    questionNumber = questionNumber,
    selectedOptionIndex = selectedOptionIndex,
    markedForReview = markedForReview,
    visited = visited,
    answeredAtEpochMs = if (selectedOptionIndex != null) System.currentTimeMillis() else null,
)

internal fun ExamSession.toEntity(): ExamSessionEntity = ExamSessionEntity(
    paperId = paperId,
    status = status.name,
    startedAtEpochMs = startedAtEpochMs,
    startedAtRealtimeMs = startedAtRealtimeMs,
    durationMins = durationMins,
    pausedAccumulatedMs = pausedAccumulatedMs,
    currentQuestionNumber = currentQuestionNumber,
    submittedAtEpochMs = null,
    language = ContentLanguage.ENGLISH.name,
)