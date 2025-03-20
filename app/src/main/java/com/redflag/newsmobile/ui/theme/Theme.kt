package com.redflag.newsmobile.ui.theme

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
    primary = Black60, // Button in general
    onPrimary = White80, // Text on button
    primaryContainer = Color.Red,
    onPrimaryContainer = Black60,

    secondary = Color.Red,
    onSecondary = White60,
    secondaryContainer = LightBlue30, // Selected icons on bottom bar
    onSecondaryContainer = Black60,

    tertiary = Beige50,
    onTertiary = Black60,
    tertiaryContainer = Beige90,
    onTertiaryContainer = Black60,

    surface = DarkBlue30, // Bottom bar, Surfaces in general
    onSurface = White60,
    surfaceVariant = Color.Red,
    onSurfaceVariant = DarkBlue80, // Icons on bottom bar
    /*
     primary = DarkBlue50,
    onPrimary = White60,
    primaryContainer = LightBlue50,
    onPrimaryContainer = Black60,

    secondary = Orange90,
    onSecondary = White60,
    secondaryContainer = Orange80,
    onSecondaryContainer = Black60,

    tertiary = Beige50,
    onTertiary = Black60,
    tertiaryContainer = Beige90,
    onTertiaryContainer = Black60,

    surface = White85,
    onSurface = Black60,
    surfaceVariant = LightGrey50,
    onSurfaceVariant = Black60,
     */
)

private val LightColorScheme = lightColorScheme(
    primary = Orange50,
    onPrimary = White60,
    primaryContainer = Orange90,
    onPrimaryContainer = Grey40,

    secondary = DarkBlue50,
    onSecondary = White60,
    secondaryContainer = Orange90,
    onSecondaryContainer = Black60,

    tertiary = Beige50,
    onTertiary = Black60,
    tertiaryContainer = Beige90,
    onTertiaryContainer = Black60,

    surface = White85,
    onSurface = Black60,
    surfaceVariant = LightGrey50,
    onSurfaceVariant = Black60,
)

@Composable
fun NewsMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        !darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}