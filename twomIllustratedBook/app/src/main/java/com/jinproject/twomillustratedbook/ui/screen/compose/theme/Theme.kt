package com.jinproject.twomillustratedbook.ui.screen.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = white,
    primaryVariant = primary,
    secondary = primary,
    background = black,
    surface = white,
    onBackground = black
)

private val LightColorPalette = lightColors(
    primary = white,
    primaryVariant = primary,
    secondary = primary,
    background = white,
    surface = white
)

@Composable
fun TwomIllustratedBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}