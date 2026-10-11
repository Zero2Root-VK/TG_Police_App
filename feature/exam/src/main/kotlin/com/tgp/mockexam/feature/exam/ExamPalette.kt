package com.tgp.mockexam.feature.exam

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.QuestionState
import com.tgp.mockexam.feature.exam.util.PaletteCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamPaletteSheet(
    totalQuestions: Int,
    answers: Map<Int, AttemptAnswer>,
    currentQuestionNumber: Int,
    onJumpTo: (Int) -> Unit,
    onCurrent: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val counts = PaletteCalculator.counts(totalQuestions, answers)

    ModalBottomSheet(
        onDismissRequest = onCurrent,
        sheetState = sheetState,
    ) {
        Column(modifier = Modifier.padding(bottom = TgpSpacing.xl)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TgpSpacing.m),
                horizontalArrangement = Arrangement.spacedBy(TgpSpacing.m),
            ) {
                LegendChip(color = MaterialTheme.colorScheme.primaryContainer, label = "Answered", count = counts.answered)
                LegendChip(color = MaterialTheme.colorScheme.errorContainer, label = "Not answered", count = counts.notAnswered)
                LegendChip(color = MaterialTheme.colorScheme.tertiaryContainer, label = "Marked", count = counts.marked)
                LegendChip(color = MaterialTheme.colorScheme.surfaceVariant, label = "Not visited", count = counts.notVisited)
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TgpSpacing.m),
                verticalArrangement = Arrangement.spacedBy(TgpSpacing.xs),
                horizontalArrangement = Arrangement.spacedBy(TgpSpacing.xs),
            ) {
                items(totalQuestions) { index ->
                    val number = index + 1
                    val questionState = PaletteCalculator.stateOf(number, answers)
                    val isCurrent = number == currentQuestionNumber
                    PaletteCell(
                        number = number,
                        questionState = questionState,
                        isCurrent = isCurrent,
                        onClick = { onJumpTo(number) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendChip(color: Color, label: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = TgpSpacing.xxs),
        )
    }
}

@Composable
private fun PaletteCell(
    number: Int,
    questionState: QuestionState,
    isCurrent: Boolean,
    onClick: () -> Unit,
) {
    val background = when (questionState) {
        QuestionState.NOT_VISITED -> MaterialTheme.colorScheme.surfaceVariant
        QuestionState.NOT_ANSWERED -> MaterialTheme.colorScheme.errorContainer
        QuestionState.ANSWERED -> MaterialTheme.colorScheme.primaryContainer
        QuestionState.MARKED_FOR_REVIEW -> MaterialTheme.colorScheme.tertiaryContainer
        QuestionState.ANSWERED_AND_MARKED -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val foreground = when (questionState) {
        QuestionState.NOT_VISITED -> MaterialTheme.colorScheme.onSurfaceVariant
        QuestionState.NOT_ANSWERED -> MaterialTheme.colorScheme.onErrorContainer
        QuestionState.ANSWERED -> MaterialTheme.colorScheme.onPrimaryContainer
        QuestionState.MARKED_FOR_REVIEW, QuestionState.ANSWERED_AND_MARKED -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    val shape = MaterialTheme.shapes.small
    Surface(
        shape = shape,
        color = background,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(shape)
            .then(
                if (isCurrent) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = shape,
                    )
                } else {
                    Modifier
                },
            )
            .clickable(onClick = onClick),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                color = foreground,
            )
        }
    }
}