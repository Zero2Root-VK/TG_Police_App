package com.tgp.mockexam.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenTrack: (Track) -> Unit,
    onBrowsePapers: () -> Unit,
    onOpenCurrentAffairs: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("TGP Mock Exam") }) },
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
                else -> HomeContent(
                    state = state,
                    onOpenTrack = onOpenTrack,
                    onBrowsePapers = onBrowsePapers,
                    onOpenCurrentAffairs = onOpenCurrentAffairs,
                    onOpenProfile = onOpenProfile,
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onOpenTrack: (Track) -> Unit,
    onBrowsePapers: () -> Unit,
    onOpenCurrentAffairs: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(TgpSpacing.m),
        verticalArrangement = Arrangement.spacedBy(TgpSpacing.m),
    ) {
        item(key = "hero") { HeroCard(state) }

        item(key = "practice-title") { SectionTitle("Practice by exam") }
        items(Track.entries.size, key = { "track-${Track.entries[it].id}" }) { index ->
            val track = Track.entries[index]
            TrackTile(
                track = track,
                papers = state.papersFor(track),
                questions = state.questionsFor(track),
                onClick = { onOpenTrack(track) },
            )
        }

        item(key = "more-title") { SectionTitle("Explore") }
        item(key = "papers") {
            ActionRow(
                icon = Icons.AutoMirrored.Filled.List,
                title = "Previous Year Papers",
                subtitle = "Browse and attempt past papers",
                onClick = onBrowsePapers,
            )
        }
        item(key = "ca") {
            ActionRow(
                icon = Icons.Filled.Notifications,
                title = "Daily Current Affairs",
                subtitle = "Coming soon",
                onClick = onOpenCurrentAffairs,
            )
        }
        item(key = "profile") {
            ActionRow(
                icon = Icons.Filled.Person,
                title = "My Profile & Purchases",
                subtitle = "Account, purchases and results",
                onClick = onOpenProfile,
            )
        }
    }
}

@Composable
private fun HeroCard(state: HomeUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Column(Modifier.padding(TgpSpacing.l)) {
            Text(
                text = "Telangana Police SI & Constable",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Practice with real previous-year papers, bilingual (English / తెలుగు)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = TgpSpacing.xs),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TgpSpacing.l),
                horizontalArrangement = Arrangement.spacedBy(TgpSpacing.xl),
            ) {
                Stat("${state.totalPapers}", "Papers")
                Stat("${state.totalQuestions}", "Questions")
                Stat("2", "Exams")
            }
        }
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = TgpSpacing.xs, start = TgpSpacing.xxs),
    )
}

@Composable
private fun TrackTile(
    track: Track,
    papers: Int,
    questions: Int,
    onClick: () -> Unit,
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(TgpSpacing.m)) {
            Text(
                text = when (track) {
                    Track.CONSTABLE -> "Constable"
                    Track.SI -> "Sub-Inspector (SI)"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "$papers papers · $questions questions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = TgpSpacing.xxs),
            )
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TgpSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TgpSpacing.m),
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
