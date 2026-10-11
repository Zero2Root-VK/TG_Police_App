package com.tgp.mockexam.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun TgpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> TgpDarkColorScheme
        else -> TgpLightColorScheme
    }
    val tgpColors = if (darkTheme) TgpDarkColors else TgpLightColors

    CompositionLocalProvider(LocalTgpColors provides tgpColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TgpTypography,
            shapes = TgpShapes,
            content = content,
        )
    }
}
