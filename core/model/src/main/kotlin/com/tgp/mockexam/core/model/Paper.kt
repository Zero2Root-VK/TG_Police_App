package com.tgp.mockexam.core.model

enum class Track(val id: String) {
    CONSTABLE("constable"),
    SI("si"),
    ;

    companion object {
        fun fromId(id: String): Track =
            entries.firstOrNull { it.id == id } ?: CONSTABLE
    }
}

enum class Stage(val id: String) {
    PRELIMS("prelims"),
    MAINS("mains"),
    ;

    companion object {
        fun fromId(id: String): Stage =
            entries.firstOrNull { it.id == id } ?: PRELIMS
    }
}

data class Solution(
    val rule: Bilingual?,
    val en: String,
    val te: String,
)

data class QuestionOption(
    val text: Bilingual,
)

data class Question(
    val number: Int,
    val prompt: Bilingual,
    val options: List<QuestionOption>,
    val answerIndex: Int,
    val solution: Solution?,
    val topic: String? = null,
    val conceptKey: String? = null,
) {
    companion object {
        const val UNKNOWN_ANSWER_INDEX = -1
    }

    val hasAnswer: Boolean get() = answerIndex >= 0
}

data class PaperSummary(
    val id: String,
    val track: Track,
    val stage: Stage,
    val year: Int,
    val paperNo: Int,
    val series: String,
    val titleEn: String,
    val titleTe: String,
    val marks: Int,
    val durationMins: Int,
    val totalQuestions: Int,
    val reviewPending: Boolean,
    val positiveMarks: Double = 1.0,
    val negativeMarks: Double = 0.0,
)

fun Paper.toSummary(): PaperSummary = PaperSummary(
    id = id,
    track = track,
    stage = stage,
    year = year,
    paperNo = paperNo,
    series = series,
    titleEn = titleEn,
    titleTe = titleTe,
    marks = marks,
    durationMins = durationMins,
    totalQuestions = totalQuestions,
    reviewPending = reviewPending,
    positiveMarks = positiveMarks,
    negativeMarks = negativeMarks,
)

data class Paper(
    val id: String,
    val track: Track,
    val stage: Stage,
    val year: Int,
    val paperNo: Int,
    val series: String,
    val titleEn: String,
    val titleTe: String,
    val marks: Int,
    val durationMins: Int,
    val totalQuestions: Int,
    val reviewPending: Boolean,
    val positiveMarks: Double = 1.0,
    val negativeMarks: Double = 0.0,
    val questions: List<Question>,
)
