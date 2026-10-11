package com.tgp.mockexam.feature.auth

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgp.mockexam.core.designsystem.component.TgpLogoBadge
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = authStrings(state.language)
    val isSignUp = state.mode == AuthMode.SIGN_UP
    val context = LocalContext.current

    // Email/password success and the social callback (completed in MainActivity) both surface here.
    LaunchedEffect(state.user) {
        if (state.user != null) onAuthenticated()
    }

    // Open the Google consent page in the browser once the ViewModel produces the URL.
    LaunchedEffect(state.browserUrl) {
        val url = state.browserUrl ?: return@LaunchedEffect
        runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
        viewModel.onBrowserLaunched()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = TgpSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(TgpSpacing.xxl))

        TgpLogoBadge(size = 76)

        Spacer(Modifier.height(TgpSpacing.l))

        Text(
            text = if (isSignUp) strings.signUpTitle else strings.signInTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = if (isSignUp) strings.signUpSubtitle else strings.signInSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = TgpSpacing.xxs),
        )

        Spacer(Modifier.height(TgpSpacing.l))

        if (isSignUp) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(strings.nameLabel) },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(TgpSpacing.s))
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text(strings.emailLabel) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(TgpSpacing.s))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text(strings.passwordLabel) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            trailingIcon = {
                Text(
                    text = if (state.passwordVisible) "Hide" else "Show",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = TgpSpacing.s)
                        .clickable(onClick = viewModel::togglePasswordVisibility),
                )
            },
            singleLine = true,
            visualTransformation = if (state.passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.error != null) {
            Text(
                text = state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TgpSpacing.xs),
            )
        }

        Spacer(Modifier.height(TgpSpacing.l))

        Button(
            onClick = { viewModel.submit(onAuthenticated) },
            enabled = state.canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text(
                    text = if (isSignUp) strings.createAccount else strings.signIn,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }

        TextButton(
            onClick = viewModel::toggleMode,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (isSignUp) strings.haveAccount else strings.noAccount)
        }

        RowOrDivider(strings.orContinue)

        OutlinedButton(
            onClick = viewModel::onGoogleSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Text(strings.continueGoogle)
        }

        if (!isSignUp) {
            TextButton(
                onClick = viewModel::sendMagicLink,
                enabled = state.canSendMagicLink,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(strings.magicLinkCta)
            }
            if (state.magicLinkSent) {
                Text(
                    text = strings.magicLinkSent,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TgpSpacing.xxs),
                )
            }
        }

        Spacer(Modifier.height(TgpSpacing.s))

        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(strings.continueGuest)
        }

        Text(
            text = strings.terms,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = TgpSpacing.m),
        )
    }
}

@Composable
private fun RowOrDivider(label: String) {
    Column(Modifier.padding(vertical = TgpSpacing.m)) {
        HorizontalDivider()
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = TgpSpacing.s),
        )
    }
}
