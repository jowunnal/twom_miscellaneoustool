package com.jinproject.features.core.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.base.item.SnackBarMessage

val LocalAnalyticsLoggingEvent = staticCompositionLocalOf {
    { event: AnalyticsEvent ->

    }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator provided")
}

val LocalShowSnackbar = staticCompositionLocalOf<(SnackBarMessage) -> Unit> {
    error("No ShowSnackbar provided")
}
