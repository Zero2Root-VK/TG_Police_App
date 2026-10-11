package com.tgp.mockexam.core.data.json

import kotlinx.serialization.Serializable

@Serializable
data class LocalizedTextDto(
    val en: String = "",
    val te: String = "",
)

@Serializable
data class RuleDto(
    val en: String = "",
    val te: String = "",
)

@Serializable
data class SolutionDto(
    val rule: RuleDto? = null,
    val en: String = "",
    val te: String = "",
)

@Serializable
data class QuestionDto(
    val n: Int,
    val prompt: LocalizedTextDto = LocalizedTextDto(),
    val options: List<LocalizedTextDto> = emptyList(),
    val answer: Int? = null,
    val solution: SolutionDto? = null,
    val topic: String? = null,
    val conceptKey: String? = null,
)

@Serializable
data class PaperDto(
    val id: String,
    val track: String,
    val stage: String,
    val year: Int,
    val paperNo: Int = 1,
    val series: String = "",
    val titleEn: String = "",
    val titleTe: String = "",
    val marks: Int = 0,
    val durationMins: Int = 0,
    val positiveMarks: Double = 1.0,
    val negativeMarks: Double = 0.0,
    val totalQuestions: Int = 0,
    val reviewPending: Boolean = true,
    val questions: List<QuestionDto> = emptyList(),
)

@Serializable
data class PaperIndexItemDto(
    val id: String,
    val track: String,
    val stage: String,
    val year: Int,
    val paperNo: Int = 1,
    val series: String = "",
    val titleEn: String = "",
    val titleTe: String = "",
    val marks: Int = 0,
    val durationMins: Int = 0,
    val positiveMarks: Double = 1.0,
    val negativeMarks: Double = 0.0,
    val totalQuestions: Int = 0,
    val reviewPending: Boolean = true,
)
