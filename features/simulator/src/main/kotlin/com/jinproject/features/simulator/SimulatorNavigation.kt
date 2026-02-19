package com.jinproject.features.simulator

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope
import kotlinx.serialization.Serializable

@Serializable
sealed class SimulatorRoute : NavKey {
    @Serializable
    data object Simulator : SimulatorRoute()
}

fun EntryProviderScope<NavKey>.simulatorEntries() {
    entry<SimulatorRoute.Simulator> {
        SimulatorScreen()
    }
}