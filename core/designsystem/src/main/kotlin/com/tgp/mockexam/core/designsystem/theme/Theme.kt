package com.tgp.mockexam.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = BlueAccent,
    secondary = GoldRestrained,
    tertiary = BlueLight,
    background = NavyDeep,
    surface = NavyLight,
    onPrimary = SurfaceWhite,
    onSecondary = NavyDeep,
    onTertiary = NavyDeep,
    onBackground = SurfaceWhite,
    onSurface = SurfaceWhite
)

val LightColorScheme = lightColorScheme(
    primary = NavyDeep,
    secondary = GoldRestrained,
    tertiary = BlueAccent,
    background = SurfaceNeutral,
    surface = SurfaceWhite,
    onPrimary = SurfaceWhite,
    onSecondary = NavyDeep,
    onTertiary = SurfaceWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary

    /* Other default colors to override
    surfaceTint = Color.White,
    onSurfaceVariant = Color.Gray,
    */
)

@Composable
fun TGPolicePrepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
