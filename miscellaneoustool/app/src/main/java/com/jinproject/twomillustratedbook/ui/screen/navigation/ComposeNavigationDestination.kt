package com.jinproject.twomillustratedbook.ui.screen.navigation

import androidx.compose.runtime.Stable

@Stable
sealed class ComposeNavigationDestination(
    val title:String,
    val route:String
) {
    data object Alarm: ComposeNavigationDestination(
        title = "알람",
        route = AlarmRoute
    )
    data object Gear: ComposeNavigationDestination(
        title = "설정",
        route = GearRoute
    )
    data object Watch: ComposeNavigationDestination(
        title = "시간",
        route = WatchRoute
    )
}

const val AlarmRoute = "alarm"
const val GearRoute = "gear"
const val WatchRoute = "watch"