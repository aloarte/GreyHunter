package com.devalr.framework.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun GreyHunterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}

private val DarkColorScheme =darkColorScheme(
    primary = HunterGreen,
    surfaceVariant = HunterGreen,
    onPrimary = TextDark,
    background = DarkGray,
    onBackground = TextLight,
    surface = DarkSurface,
    onSurface = TextLight
)

private val LightColorScheme = lightColorScheme(
    primary = HunterGreen,
    surfaceVariant = HunterGreenLight,
    background = WhiteGray,
    onBackground = TextDark,
    surface = LightGray,
    onSurface = TextDark
)

