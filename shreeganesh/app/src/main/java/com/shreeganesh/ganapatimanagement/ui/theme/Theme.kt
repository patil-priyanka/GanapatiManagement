package com.shreeganesh.ganapatimanagement.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SaffronLight,
    onPrimary = Color.Black,
    primaryContainer = SaffronContainerDark,
    onPrimaryContainer = SaffronLight,
    secondary = GoldLight,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF423000),
    onSecondaryContainer = GoldLight,
    tertiary = VermilionRed,
    onTertiary = Color.White,
    background = FestiveBackgroundDark,
    onBackground = TextPrimaryDark,
    surface = FestiveSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = FestiveSurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = FestiveBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronContainer,
    onPrimaryContainer = SaffronDeep,
    secondary = MarigoldGold,
    onSecondary = Color.Black,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = Color(0xFF5D4200),
    tertiary = VermilionRed,
    onTertiary = Color.White,
    tertiaryContainer = VermilionContainer,
    onTertiaryContainer = VermilionDeep,
    background = FestiveBackgroundLight,
    onBackground = TextPrimaryLight,
    surface = FestiveSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = FestiveSurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = FestiveBorderLight
)

@Composable
fun GanapatiManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep dynamicColor false by default to preserve the auspicious Ganapati festive palette
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}