package com.jinproject.core.util

import android.content.Context
import java.util.Locale

fun <T> Context.doOnLocaleLanguage(onKo: T, onElse: T) =
    when (this.resources.configuration.locales[0].language) {
        "ko" -> onKo
        else -> onElse
    }

fun isKorean() = Locale.getDefault().language == "ko"

fun String.onLanguage(ko: String, en: String) = if (this == "ko")
    ko
else
    en