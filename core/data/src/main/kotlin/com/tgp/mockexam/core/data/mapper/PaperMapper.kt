package com.tgp.mockexam.core.data.mapper

import com.tgp.mockexam.core.data.json.LocalizedTextDto
import com.tgp.mockexam.core.data.json.PaperDto
import com.tgp.mockexam.core.data.json.PaperIndexItemDto
import com.tgp.mockexam.core.data.json.QuestionDto
import com.tgp.mockexam.core.data.json.SolutionDto
import com.tgp.mockexam.core.model.Bilingual
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.PaperSummary
import com.tgp.mockexam.core.model.Question
import com.tgp.mockexam.core.model.QuestionOption
import com.tgp.mockexam.core.model.Solution
import com.tgp.mockexam.core.model.Stage
import com.tgp.mockexam.core.model.Track

private fun LocalizedTextDto.toBilingual() = Bilingual(en = en, te = te)

private fun SolutionDto.toDomain(): Solution = Solution(
    rule = rule?.let { Bilingual(en = it.en, te = it.te) },
    en = en,
    te = te,
)

private fun QuestionDto.toDomain(): Question {
    val index = when {
        answer == null || answer <= 0 -> Question.UNKNOWN_ANSWER_INDEX
        else -> answer - 1
    }
    return Question(
        number = n,
        prompt = prompt.toBilingual(),
        options = options.map { QuestionOption(it.toBilingual()) },
        answerIndex = index,
        solution = solution?.toDomain(),
        topic = topic,
        conceptKey = conceptKey,
    )
}

fun PaperDto.toDomain(): Paper = Paper(
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

fun PaperIndexItemDto.toDomain(): PaperSummary = PaperSummary(
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
