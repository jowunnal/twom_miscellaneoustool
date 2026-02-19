package com.jinproject.features.alarm

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Stable
interface AlarmRoute : NavKey {
    @Serializable
    data object Alarm : AlarmRoute
}
