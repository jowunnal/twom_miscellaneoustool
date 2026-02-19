package com.jinproject.features.home

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope
import kotlinx.serialization.Serializable

@Serializable
sealed class HomeRoute : NavKey {
    @Serializable
    data object Home : HomeRoute()
}

fun EntryProviderScope<NavKey>.homeEntries() {
    entry<HomeRoute.Home> {
        HomeScreen()
    }
}
