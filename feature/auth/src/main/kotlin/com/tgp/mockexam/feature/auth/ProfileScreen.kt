package com.tgp.mockexam.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgp.mockexam.core.designsystem.component.TgpShield
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.AuthUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignIn: () -> Unit,
    onOpenAdmin: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = authStrings(state.language)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(strings.profileTitle) }) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(TgpSpacing.l),
        ) {
            val user = state.user
            if (user == null) {
                SignedOutProfile(strings, onSignIn, Modifier.align(Alignment.Center))
            } else {
                SignedInProfile(strings, user, state.role, viewModel::signOut, onOpenAdmin)
            }
        }
    }
}

@Composable
private fun SignedOutProfile(
    strings: AuthStrings,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TgpShield(modifier = Modifier.size(72.dp))
        Spacer(Modifier.height(TgpSpacing.m))
        Text(
            text = strings.signInCta,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = strings.signInCtaSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = TgpSpacing.xs),
        )
        Spacer(Modifier.height(TgpSpacing.l))
        Button(onClick = onSignIn) {
            Text(strings.signIn)
        }
    }
}

@Composable
private fun SignedInProfile(
    strings: AuthStrings,
    user: AuthUser,
    role: String,
    onSignOut: () -> Unit,
    onOpenAdmin: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials(user.name.ifBlank { user.email }),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(Modifier.height(TgpSpacing.m))
        Text(
            text = user.name.ifBlank { user.email.substringBefore("@") },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(TgpSpacing.l))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(TgpSpacing.m),
                verticalArrangement = Arrangement.spacedBy(TgpSpacing.xs),
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = strings.signedInAs,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    Text(user.id, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        if (role == "admin") {
            Spacer(Modifier.height(TgpSpacing.s))
            Button(
                onClick = onOpenAdmin,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Admin Console")
            }
        }

        Spacer(Modifier.height(TgpSpacing.l))

        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(strings.signOut)
        }
    }
}

private fun initials(source: String): String {
    val parts = source.trim().split(Regex("[\\s.@_]+")).filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(1).uppercase()
        else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
}
