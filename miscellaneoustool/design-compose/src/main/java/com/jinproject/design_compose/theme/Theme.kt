package com.jinproject.design_compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_background
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_error
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_onBackground
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_onError
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_onPrimary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_onSecondary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_onSurface
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_onSurfaceVariant
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_outline
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_primary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_scrim
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_secondary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.dark_surface
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_background
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_error
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_onBackground
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_onError
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_onPrimary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_onSecondary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_onSurface
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_onSurfaceVariant
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_outline
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_primary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_scrim
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_secondary
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.light_surface
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.red

@Stable
private val DarkColorPalette = darkColorScheme(
    primary = dark_primary.color,
    onPrimary = dark_onPrimary.color,
    primaryContainer = red.color,
    onPrimaryContainer = red.color,
    secondary = dark_secondary.color,
    onSecondary = dark_onSecondary.color,
    secondaryContainer = red.color,
    onSecondaryContainer = red.color,
    tertiary = red.color,
    onTertiary = red.color,
    tertiaryContainer = red.color,
    onTertiaryContainer = red.color,
    background = dark_background.color,
    onBackground = dark_onBackground.color,
    surface = dark_surface.color,
    onSurface = dark_onSurface.color,
    surfaceVariant = red.color,
    onSurfaceVariant = dark_onSurfaceVariant.color,
    inverseSurface = red.color,
    inverseOnSurface = red.color,
    error = dark_error.color,
    errorContainer = red.color,
    onError = dark_onError.color,
    onErrorContainer = red.color,
    scrim = dark_scrim.color,
    outline = dark_outline.color
)

@Stable
private val LightColorPalette = lightColorScheme(
    primary = light_primary.color,
    onPrimary = light_onPrimary.color,
    secondary = light_secondary.color,
    onSecondary = light_onSecondary.color,
    background = light_background.color,
    onBackground = light_onBackground.color,
    surface = light_surface.color,
    onSurface = light_onSurface.color,
    onSurfaceVariant = light_onSurfaceVariant.color,
    error = light_error.color,
    onError = light_onError.color,
    scrim = light_scrim.color,
    outline = light_outline.color
)

@Composable
fun MiscellaneousToolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}