package com.tgp.mockexam.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand seed palette — Telangana Police identity (navy / khaki / gold).
private val Navy80 = Color(0xFFAAC7FF)
private val Navy40 = Color(0xFF1B3A6B)
private val Khaki80 = Color(0xFFC3C8A0)
private val Khaki40 = Color(0xFF5A5F3A)
private val Gold80 = Color(0xFFEFC000)
private val Gold40 = Color(0xFF8A6A00)

val TgpLightColorScheme = lightColorScheme(
    primary = Navy40,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD6E3FF),
    onPrimaryContainer = Color(0xFF001B3D),
    secondary = Khaki40,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDFE3B3),
    onSecondaryContainer = Color(0xFF1B1E00),
    tertiary = Gold40,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE08A),
    onTertiaryContainer = Color(0xFF2A1C00),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFBF8FF),
    onBackground = Color(0xFF1A1B20),
    surface = Color(0xFFFBF8FF),
    onSurface = Color(0xFF1A1B20),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44464F),
    outline = Color(0xFF757780),
    outlineVariant = Color(0xFFC5C6D0),
    inverseSurface = Color(0xFF2F3036),
    inverseOnSurface = Color(0xFFF1F0F7),
    inversePrimary = Navy80,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F2FA),
    surfaceContainer = Color(0xFFEFEDF4),
    surfaceContainerHigh = Color(0xFFE9E7EF),
    surfaceContainerHighest = Color(0xFFE3E1E9),
)

val TgpDarkColorScheme = darkColorScheme(
    primary = Navy80,
    onPrimary = Color(0xFF002F65),
    primaryContainer = Color(0xFF0F3F7A),
    onPrimaryContainer = Color(0xFFD6E3FF),
    secondary = Khaki80,
    onSecondary = Color(0xFF2D3210),
    secondaryContainer = Color(0xFF434828),
    onSecondaryContainer = Color(0xFFDFE3B3),
    tertiary = Gold80,
    onTertiary = Color(0xFF473100),
    tertiaryContainer = Color(0xFF664E00),
    onTertiaryContainer = Color(0xFFFFE08A),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF111318),
    onBackground = Color(0xFFE2E2E9),
    surface = Color(0xFF111318),
    onSurface = Color(0xFFE2E2E9),
    surfaceVariant = Color(0xFF44464F),
    onSurfaceVariant = Color(0xFFC5C6D0),
    outline = Color(0xFF8F9099),
    outlineVariant = Color(0xFF44464F),
    inverseSurface = Color(0xFFE2E2E9),
    inverseOnSurface = Color(0xFF2F3036),
    inversePrimary = Navy40,
    surfaceContainerLowest = Color(0xFF0C0E13),
    surfaceContainerLow = Color(0xFF191C20),
    surfaceContainer = Color(0xFF1D2024),
    surfaceContainerHigh = Color(0xFF282A2F),
    surfaceContainerHighest = Color(0xFF33353A),
)

/**
 * Semantic colors for exam states that Material roles do not cover.
 * Never signal correctness with color alone — pair with an icon/label.
 */
@Immutable
data class TgpColors(
    val correct: Color,
    val onCorrect: Color,
    val correctContainer: Color,
    val onCorrectContainer: Color,
    val incorrect: Color,
    val onIncorrect: Color,
    val incorrectContainer: Color,
    val onIncorrectContainer: Color,
    val marked: Color,
    val onMarked: Color,
    val markedContainer: Color,
    val onMarkedContainer: Color,
    val premium: Color,
)

val TgpLightColors = TgpColors(
    correct = Color(0xFF2E7D32),
    onCorrect = Color(0xFFFFFFFF),
    correctContainer = Color(0xFFB8F0B4),
    onCorrectContainer = Color(0xFF002204),
    incorrect = Color(0xFFBA1A1A),
    onIncorrect = Color(0xFFFFFFFF),
    incorrectContainer = Color(0xFFFFDAD6),
    onIncorrectContainer = Color(0xFF410002),
    marked = Color(0xFF8A6A00),
    onMarked = Color(0xFFFFFFFF),
    markedContainer = Color(0xFFFFE08A),
    onMarkedContainer = Color(0xFF2A1C00),
    premium = Color(0xFF8A6A00),
)

val TgpDarkColors = TgpColors(
    correct = Color(0xFF8BD68C),
    onCorrect = Color(0xFF003909),
    correctContainer = Color(0xFF0F5215),
    onCorrectContainer = Color(0xFFB8F0B4),
    incorrect = Color(0xFFFFB4AB),
    onIncorrect = Color(0xFF690005),
    incorrectContainer = Color(0xFF93000A),
    onIncorrectContainer = Color(0xFFFFDAD6),
    marked = Color(0xFFEFC000),
    onMarked = Color(0xFF473100),
    markedContainer = Color(0xFF664E00),
    onMarkedContainer = Color(0xFFFFE08A),
    premium = Color(0xFFEFC000),
)

val LocalTgpColors = staticCompositionLocalOf { TgpLightColors }
