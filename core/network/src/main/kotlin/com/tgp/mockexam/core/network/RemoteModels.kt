package com.tgp.mockexam.core.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/** Response of GET /tablesdb/{db}/tables/{table}/rows */
@Serializable
data class RemoteRows(
    val total: Long = 0,
    val rows: List<JsonObject> = emptyList(),
)

/** Raw row of the `papers` table (rowId == paper id). */
class RemotePaperRow(private val row: JsonObject) {
    val id: String get() = row.string("id") ?: row.string("\u0024id").orEmpty()
    val track: String? get() = row.string("track")
    val stage: String? get() = row.string("stage")
    val year: Int get() = row.int("year")
    val paperNo: Int get() = row.int("paperNo")
    val series: String get() = row.string("series").orEmpty()
    val titleEn: String get() = row.string("titleEn").orEmpty()
    val titleTe: String get() = row.string("titleTe").orEmpty()
    val marks: Int get() = row.int("marks")
    val durationMins: Int get() = row.int("durationMins")
    val positiveMarks: Double get() = row.double("positiveMarks", fallback = 1.0)
    val negativeMarks: Double get() = row.double("negativeMarks", fallback = 0.0)
    val totalQuestions: Int get() = row.int("totalQuestions")
    val reviewPending: Boolean get() = row.bool("reviewPending")
    val isFree: Boolean get() = row.bool("isFree")
    val contentHash: String get() = row.string("contentHash").orEmpty()
    val updatedAt: String get() = row.string("updatedAt").orEmpty()
}

/** Raw row of the `questions` table; identified by (paperId, n). */
class RemoteDailyCaRow(private val row: JsonObject) {
    val id: String get() = row.string("id") ?: row.string("$id").orEmpty()
    val date: String get() = row.string("date").orEmpty()
    val subject: String get() = row.string("subject").orEmpty()
    val access: String get() = row.string("access").orEmpty()
    val questionsJson: String get() = row.string("questionsJson").orEmpty()
    val publishedAt: String get() = row.string("publishedAt").orEmpty()
}
class RemoteQuestionRow(private val row: JsonObject) {
    val paperId: String get() = row.string("paperId").orEmpty()
    val n: Int get() = row.int("n")
    val promptEn: String get() = row.string("promptEn").orEmpty()
    val promptTe: String get() = row.string("promptTe").orEmpty()
    val optionsEn: String get() = row.string("optionsEn").orEmpty()
    val optionsTe: String get() = row.string("optionsTe").orEmpty()
    val answer: Int get() = row.int("answer")
    val solutionEn: String get() = row.string("solutionEn").orEmpty()
    val solutionTe: String get() = row.string("solutionTe").orEmpty()
    val ruleEn: String get() = row.string("ruleEn").orEmpty()
    val ruleTe: String get() = row.string("ruleTe").orEmpty()
    val topic: String get() = row.string("topic").orEmpty()
    val conceptKey: String get() = row.string("conceptKey").orEmpty()
    val version: Int get() = row.int("version", fallback = 1)
    val status: String get() = row.string("status").orEmpty()
    val updatedAt: String get() = row.string("updatedAt").orEmpty()
}

private fun JsonObject.string(key: String): String? =
    this[key]?.let { if (it is kotlinx.serialization.json.JsonPrimitive && it.isString) it.content else null }

private fun JsonObject.int(key: String, fallback: Int = 0): Int =
    (this[key] as? JsonPrimitive)?.let { it.contentOrNull?.toIntOrNull() } ?: fallback

private fun JsonObject.double(key: String, fallback: Double = 0.0): Double =
    (this[key] as? JsonPrimitive)?.let { it.contentOrNull?.toDoubleOrNull() } ?: fallback

private fun JsonObject.bool(key: String): Boolean =
    (this[key] as? JsonPrimitive)?.let { it.contentOrNull?.toBooleanStrictOrNull() } ?: false