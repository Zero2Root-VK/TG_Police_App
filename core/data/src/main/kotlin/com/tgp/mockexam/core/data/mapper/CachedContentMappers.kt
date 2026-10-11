package com.tgp.mockexam.core.data.mapper

import com.tgp.mockexam.core.database.model.CachedPaperEntity
import com.tgp.mockexam.core.database.model.CachedQuestionEntity
import com.tgp.mockexam.core.model.Bilingual
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.PaperSummary
import com.tgp.mockexam.core.model.Question
import com.tgp.mockexam.core.model.QuestionOption
import com.tgp.mockexam.core.model.Solution
import com.tgp.mockexam.core.model.Stage
import com.tgp.mockexam.core.model.Track
import com.tgp.mockexam.core.network.RemotePaperRow
import com.tgp.mockexam.core.network.RemoteQuestionRow
import kotlinx.serialization.json.Json

private val instanceJson = Json { ignoreUnknownKeys = true; isLenient = true }

internal fun RemotePaperRow.toEntity(): CachedPaperEntity = CachedPaperEntity(
    id = id,
    track = track.orEmpty(),
    stage = stage.orEmpty(),
    year = year,
    paperNo = paperNo,
    series = series,
    titleEn = titleEn,
    titleTe = titleTe,
    marks = marks,
    durationMins = durationMins,
    positiveMarks = positiveMarks,
    negativeMarks = negativeMarks,
    totalQuestions = totalQuestions,
    reviewPending = reviewPending,
    isFree = isFree,
    contentHash = contentHash,
    updatedAt = updatedAt,
)

internal fun RemoteQuestionRow.toEntity(): CachedQuestionEntity = CachedQuestionEntity(
    paperId = paperId,
    n = n,
    promptEn = promptEn,
    promptTe = promptTe,
    optionsEn = optionsEn,
    optionsTe = optionsTe,
    answer = answer,
    solutionEn = solutionEn,
    solutionTe = solutionTe,
    ruleEn = ruleEn,
    ruleTe = ruleTe,
    topic = topic,
    conceptKey = conceptKey,
    version = if (version > 0) version else 1,
    status = status,
    updatedAt = updatedAt,
)

internal fun CachedPaperEntity.toSummary(): PaperSummary = PaperSummary(
    id = id,
    track = Track.fromId(track),
    stage = Stage.fromId(stage),
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

internal fun CachedPaperEntity.toDomain(questions: List<CachedQuestionEntity>): Paper = Paper(
    id = id,
    track = Track.fromId(track),
    stage = Stage.fromId(stage),
    year = year,
    paperNo = paperNo,
    series = series,
    titleEn = titleEn,
    titleTe = titleTe,
    marks = marks,
    durationMins = durationMins,
    totalQuestions = if (totalQuestions > 0) totalQuestions else questions.size,
    reviewPending = reviewPending,
    positiveMarks = positiveMarks,
    negativeMarks = negativeMarks,
    questions = questions.map { it.toDomain() },
)

private fun decodeOptions(encodedEn: String, encodedTe: String): List<QuestionOption> {
    val en: List<String> = runCatching { instanceJson.decodeFromString<List<String>>(encodedEn) }.getOrDefault(emptyList())
    val te: List<String> = runCatching { instanceJson.decodeFromString<List<String>>(encodedTe) }.getOrDefault(emptyList())
    val count = maxOf(en.size, te.size)
    return List(count) { index ->
        QuestionOption(
            text = Bilingual(en = en.getOrElse(index) { "" }, te = te.getOrElse(index) { "" }),
        )
    }
}

internal fun CachedQuestionEntity.toDomain(): Question = Question(
    number = n,
    prompt = Bilingual(en = promptEn, te = promptTe),
    options = decodeOptions(optionsEn, optionsTe),
    answerIndex = if (answer >= 1) answer - 1 else Question.UNKNOWN_ANSWER_INDEX,
    solution = Solution(
        rule = if (ruleEn.isNotEmpty() || ruleTe.isNotEmpty()) Bilingual(en = ruleEn, te = ruleTe) else null,
        en = solutionEn,
        te = solutionTe,
    ),
    topic = topic.ifEmpty { null },
    conceptKey = conceptKey.ifEmpty { null },
)