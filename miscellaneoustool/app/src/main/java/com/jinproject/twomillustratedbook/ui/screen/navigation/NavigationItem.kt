package com.jinproject.twomillustratedbook.ui.screen.navigation

import androidx.compose.runtime.Stable

@Stable
sealed class NavigationItem(
    val title:String,
    val route:String
) {
    data object Alarm: NavigationItem(
        title = "알람",
        route = "alarm"
    )
    data object Gear: NavigationItem(
        title = "설정",
        route = "gear"
    )
    data object Watch: NavigationItem(
        title = "시간",
        route = "watch"
    )
}