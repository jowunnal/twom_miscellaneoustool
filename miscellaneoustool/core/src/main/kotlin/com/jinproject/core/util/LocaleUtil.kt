package com.jinproject.core.util

import android.content.Context

fun <T> Context.doOnLocaleLanguage(onKo: T, onElse: T) =
    when (this.resources.configuration.locales[0].language) {
        "ko" -> onKo
        else -> onElse
    }