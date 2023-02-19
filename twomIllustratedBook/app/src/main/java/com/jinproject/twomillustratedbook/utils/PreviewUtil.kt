package com.jinproject.twomillustratedbook.utils

import androidx.compose.runtime.Composable
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.TwomIllustratedBookTheme

@Composable
fun TwomIllustratedBookPreview(content: @Composable () -> Unit) {
    TwomIllustratedBookTheme() { content() }
}