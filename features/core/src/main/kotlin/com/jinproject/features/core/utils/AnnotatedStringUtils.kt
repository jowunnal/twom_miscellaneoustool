package com.jinproject.features.core.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun AnnotatedString.Builder.appendBoldText(text: String, color: Color) {
    withStyle(
        SpanStyle(
            fontWeight = FontWeight.Bold,
            color = color
        )
    ) {
        append(text)
    }
}