package com.tgp.mockexam.feature.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.PaperSummary
import com.tgp.mockexam.core.model.Stage
import com.tgp.mockexam.core.model.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onPaperClick: (PaperSummary) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Previous Year Papers") }) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                state.loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text(
                    text = state.error ?: "Error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(TgpSpacing.l),
                )
                else -> CatalogList(
                    papers = state.papers,
                    onPaperClick = onPaperClick,
                )
            }
        }
    }
}

@Composable
private fun CatalogList(
    papers: List<PaperSummary>,
    onPaperClick: (PaperSummary) -> Unit,
) {
    val grouped = papers.groupBy { it.track }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(TgpSpacing.s),
        verticalArrangement = Arrangement.spacedBy(TgpSpacing.s),
    ) {
        Track.entries.forEach { track ->
            val trackPapers = grouped[track].orEmpty()
            if (trackPapers.isEmpty()) return@forEach

            item(key = "track-${track.id}") {
                Text(
                    text = trackLabel(track),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(
                        start = TgpSpacing.xs,
                        top = TgpSpacing.s,
                        bottom = TgpSpacing.xxs,
                    ),
                )
            }

            Stage.entries.forEach { stage ->
                val stagePapers = trackPapers
                    .filter { it.stage == stage }
                    .sortedBy { it.year }
                if (stagePapers.isEmpty()) return@forEach

                item(key = "stage-${track.id}-${stage.id}") {
                    Text(
                        text = stageLabel(stage),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = TgpSpacing.xs, top = TgpSpacing.xs),
                    )
                }

                items(stagePapers, key = { it.id }) { paper ->
                    PaperCard(paper = paper, onClick = { onPaperClick(paper) })
                }
            }
        }
    }
}

@Composable
private fun PaperCard(paper: PaperSummary, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(TgpSpacing.m)) {
            Text(
                text = paper.titleEn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            if (paper.titleTe.isNotBlank()) {
                Text(
                    text = paper.titleTe,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TgpSpacing.s),
                horizontalArrangement = Arrangement.spacedBy(TgpSpacing.l),
            ) {
                Meta("Year", "${paper.year}")
                Meta("Questions", "${paper.totalQuestions}")
                Meta("Marks", "${paper.marks}")
                Meta("Time", "${paper.durationMins}m")
            }
        }
    }
}

@Composable
private fun Meta(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun trackLabel(track: Track): String = when (track) {
    Track.CONSTABLE -> "Constable"
    Track.SI -> "Sub-Inspector (SI)"
}

private fun stageLabel(stage: Stage): String = when (stage) {
    Stage.PRELIMS -> "Prelims"
    Stage.MAINS -> "Mains"
}
