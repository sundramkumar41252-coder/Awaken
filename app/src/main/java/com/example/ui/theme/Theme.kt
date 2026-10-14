package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AwakenColorScheme = darkColorScheme(
    primary = AwakenVioletPrimary,
    onPrimary = AwakenTextWhite,
    primaryContainer = AwakenCardBgElevated,
    onPrimaryContainer = AwakenVioletLight,
    secondary = AwakenHoloCyan,
    onSecondary = AwakenBackground,
    secondaryContainer = AwakenSurfaceDark,
    onSecondaryContainer = AwakenHoloCyanBright,
    tertiary = AwakenVioletLight,
    onTertiary = AwakenBackground,
    background = AwakenBackground,
    onBackground = AwakenTextWhite,
    surface = AwakenSurfaceDark,
    onSurface = AwakenTextWhite,
    surfaceVariant = AwakenCardBg,
    onSurfaceVariant = AwakenTextSecondary,
    outline = AwakenCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent Awaken theme regardless of device dynamic color
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = AwakenBackground.toArgb()
                window.navigationBarColor = AwakenBackground.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = AwakenColorScheme,
        typography = Typography,
        content = content
    )
}
