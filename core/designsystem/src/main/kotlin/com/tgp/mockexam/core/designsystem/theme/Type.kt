package com.tgp.mockexam.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.core.designsystem.R

/**
 * Brand font family. `Noto Sans Telugu` is bundled (variable font) so both Latin and Telugu
 * render identically across OEMs — never rely on device script fonts. Weights are mapped onto
 * the variable font's `wght` axis.
 */
private fun telugu(weight: FontWeight, axis: Int): Font = Font(
    resId = R.font.noto_sans_telugu,
    weight = weight,
    style = FontStyle.Normal,
    variationSettings = FontVariation.Settings(FontVariation.weight(axis)),
)

val TgpFontFamily: FontFamily = FontFamily(
    telugu(FontWeight.Normal, 400),
    telugu(FontWeight.Medium, 500),
    telugu(FontWeight.SemiBold, 600),
    telugu(FontWeight.Bold, 700),
)

private fun tgpStyle(
    size: Int,
    lineHeight: Int,
    weight: FontWeight,
    letterSpacing: Double = 0.0,
    tabular: Boolean = false,
): TextStyle = TextStyle(
    fontFamily = TgpFontFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.sp,
    fontFeatureSettings = if (tabular) "tnum" else null,
)

val TgpTypography = Typography(
    displayLarge = tgpStyle(57, 64, FontWeight.Normal, letterSpacing = -0.25, tabular = true),
    displayMedium = tgpStyle(45, 52, FontWeight.Normal, tabular = true),
    displaySmall = tgpStyle(36, 44, FontWeight.Bold, tabular = true),
    headlineLarge = tgpStyle(32, 40, FontWeight.SemiBold),
    headlineMedium = tgpStyle(28, 36, FontWeight.SemiBold),
    headlineSmall = tgpStyle(24, 32, FontWeight.SemiBold, tabular = true),
    titleLarge = tgpStyle(22, 28, FontWeight.SemiBold),
    titleMedium = tgpStyle(16, 24, FontWeight.Medium, letterSpacing = 0.15),
    titleSmall = tgpStyle(14, 20, FontWeight.Medium, letterSpacing = 0.1),
    bodyLarge = tgpStyle(16, 24, FontWeight.Normal, letterSpacing = 0.5),
    bodyMedium = tgpStyle(14, 20, FontWeight.Normal, letterSpacing = 0.25),
    bodySmall = tgpStyle(12, 16, FontWeight.Normal, letterSpacing = 0.4),
    labelLarge = tgpStyle(14, 20, FontWeight.Medium, letterSpacing = 0.1),
    labelMedium = tgpStyle(12, 16, FontWeight.Medium, letterSpacing = 0.5),
    labelSmall = tgpStyle(11, 16, FontWeight.Medium, letterSpacing = 0.5),
)
