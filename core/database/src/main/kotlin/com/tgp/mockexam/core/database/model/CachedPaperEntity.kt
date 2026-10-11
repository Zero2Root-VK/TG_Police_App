package com.tgp.mockexam.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Mirrors the `papers` row from Appwrite TablesDB. */
@Entity(tableName = "cached_papers")
data class CachedPaperEntity(
    @PrimaryKey val id: String,
    val track: String,
    val stage: String,
    val year: Int,
    val paperNo: Int,
    val series: String,
    val titleEn: String,
    val titleTe: String,
    val marks: Int,
    val durationMins: Int,
    val positiveMarks: Double = 1.0,
    val negativeMarks: Double = 0.0,
    val totalQuestions: Int,
    val reviewPending: Boolean,
    val isFree: Boolean,
    val contentHash: String,
    val updatedAt: String,
)