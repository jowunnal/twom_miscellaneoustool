package com.jinproject.core.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
fun <T>Context.doOnLocaleLanguage(onKo: T, onElse: T) = when(this.resources.configuration.locales[0].language) {
    "ko" -> onKo
    else -> onElse
}