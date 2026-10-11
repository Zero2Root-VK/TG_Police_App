package com.tgp.mockexam.core.data.mapper

import com.tgp.mockexam.core.database.model.ExamSessionEntity

internal fun entityOf(
    paperId: String,
    status: String,
    startedAtEpochMs: Long,
    startedAtRealtimeMs: Long,
    durationMins: Int,
    pausedAccumulatedMs: Long,
    currentQuestionNumber: Int,
    submittedAtEpochMs: Long?,
    language: String,
): ExamSessionEntity = ExamSessionEntity(
    paperId = paperId,
    status = status,
    startedAtEpochMs = startedAtEpochMs,
    startedAtRealtimeMs = startedAtRealtimeMs,
    durationMins = durationMins,
    pausedAccumulatedMs = pausedAccumulatedMs,
    currentQuestionNumber = currentQuestionNumber,
    submittedAtEpochMs = submittedAtEpochMs,
    language = language,
)