package com.jinproject.features.core.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.jinproject.features.core.AnalyticsEvent

val LocalAnalyticsLoggingEvent = staticCompositionLocalOf {
    { event: AnalyticsEvent ->

    }
}