package com.jinproject.features.simulator

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.core.Route
import kotlinx.serialization.Serializable

@Serializable
sealed class SimulatorRoute: Route {
    @Serializable
    data object SimulatorGraph: SimulatorRoute()

    @Serializable
    data object Simulator : SimulatorRoute()
}

fun NavGraphBuilder.simulatorNavGraph() {
    navigation<SimulatorRoute.SimulatorGraph>(
        startDestination = SimulatorRoute.Simulator
    ) {
        composable<SimulatorRoute.Simulator> {
            SimulatorScreen()
        }
    }
}