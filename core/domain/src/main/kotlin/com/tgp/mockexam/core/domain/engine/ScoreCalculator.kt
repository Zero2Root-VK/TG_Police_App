package com.tgp.mockexam.core.domain.engine

import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.Paper

data class ExamResult(
    val score: Double,
    val maxMarks: Double,
    val correct: Int,
    val incorrect: Int,
    val skipped: Int,
    val totalQuestions: Int,
    val timeTakenMs: Long,
    val accuracyPercent: Double,
)

object ScoreCalculator {

    fun compute(paper: Paper, answers: Map<Int, AttemptAnswer>, timeTakenMs: Long): ExamResult {
        var correct = 0
        var incorrect = 0
        var score = 0.0

        paper.questions.forEach { question ->
            val answer = answers[question.number]
            when {
                answer == null || answer.selectedOptionIndex == null -> Unit
                !question.hasAnswer -> Unit
                answer.selectedOptionIndex == question.answerIndex -> {
                    correct++
                    score += paper.positiveMarks
                }

                else -> {
                    incorrect++
                    score -= paper.negativeMarks
                }
            }
        }

        val attempted = correct + incorrect
        val accuracy = if (attempted == 0) 0.0 else correct.toDouble() / attempted * 100

        return ExamResult(
            score = score,
            maxMarks = paper.totalQuestions * paper.positiveMarks,
            correct = correct,
            incorrect = incorrect,
            skipped = paper.totalQuestions - attempted,
            totalQuestions = paper.totalQuestions,
            timeTakenMs = timeTakenMs,
            accuracyPercent = accuracy,
        )
    }
}