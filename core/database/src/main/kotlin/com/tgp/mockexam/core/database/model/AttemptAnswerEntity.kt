package com.tgp.mockexam.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "attempt_answers",
    primaryKeys = ["paperId", "questionNumber"],
    foreignKeys = [
        ForeignKey(
            entity = ExamSessionEntity::class,
            parentColumns = ["paperId"],
            childColumns = ["paperId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("paperId")],
)
data class AttemptAnswerEntity(
    val paperId: String,
    val questionNumber: Int,
    val selectedOptionIndex: Int?,
    val markedForReview: Boolean,
    val visited: Boolean,
    val answeredAtEpochMs: Long?,
)