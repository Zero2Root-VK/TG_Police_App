package com.tgp.mockexam.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_sessions")
data class ExamSessionEntity(
    @PrimaryKey
    val paperId: String,
    val status: String,
    val startedAtEpochMs: Long,
    val startedAtRealtimeMs: Long,
    val durationMins: Int,
    val pausedAccumulatedMs: Long,
    val currentQuestionNumber: Int,
    val submittedAtEpochMs: Long?,
    val language: String,
)