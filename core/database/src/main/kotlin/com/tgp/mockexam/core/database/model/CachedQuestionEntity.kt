package com.tgp.mockexam.core.database.model

import androidx.room.Entity

/** Mirrors the `questions` row from Appwrite TablesDB; identified by (paperId, n). */
@Entity(
    tableName = "cached_questions",
    primaryKeys = ["paperId", "n"],
)
data class CachedQuestionEntity(
    val paperId: String,
    val n: Int,
    val promptEn: String,
    val promptTe: String,
    val optionsEn: String,
    val optionsTe: String,
    val answer: Int,
    val solutionEn: String,
    val solutionTe: String,
    val ruleEn: String,
    val ruleTe: String,
    val topic: String,
    val conceptKey: String,
    val version: Int,
    val status: String,
    val updatedAt: String,
)