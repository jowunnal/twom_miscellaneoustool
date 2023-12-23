package com.jinproject.design_compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.jinproject.design_ui.R
import com.jinproject.design_compose.tu

@Stable
val font = FontFamily(
    Font(R.font.nanum_gothic_bold, FontWeight.Bold),
    Font(R.font.nanum_gothic_medium, FontWeight.Medium),
    Font(R.font.nanum_gothic_light, FontWeight.Light)
)

@Stable
val Typography @Composable get() = Typography(
    headlineLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 30.tu,
        lineHeight = 31.tu
    ),
    headlineMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 27.tu,
        lineHeight = 29.tu
    ),
    headlineSmall = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 25.tu,
        lineHeight = 27.tu
    ),
    titleLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 24.tu,
        lineHeight = 26.tu
    ),
    titleMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 21.tu,
        lineHeight = 23.tu
    ),
    titleSmall = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 18.tu,
        lineHeight = 20.tu
    ),
    bodyLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Medium,
        fontSize = 16.tu,
        lineHeight = 18.tu
    ),
    bodyMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Medium,
        fontSize = 14.tu,
        lineHeight = 16.tu
    ),
    bodySmall = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Medium,
        fontSize = 12.tu,
        lineHeight = 14.tu
    ),
    labelLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Medium,
        fontSize = 16.tu,
        lineHeight = 18.tu,
        textDecoration = TextDecoration.Underline
    ),
    labelSmall = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Light,
        fontSize = 10.tu,
        lineHeight = 12.tu
    )
)