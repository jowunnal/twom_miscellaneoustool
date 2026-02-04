package com.jinproject.features.simulator

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.core.compose.Route
import com.jinproject.features.core.compose.TopLevelRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class SimulatorRoute: Route {
    @Serializable
    data object SimulatorGraph: SimulatorRoute()

    @Serializable
    data object Simulator : SimulatorRoute(), TopLevelRoute {
        override val icon: Int = com.jinproject.design_ui.R.drawable.icon_simulator
        override val iconClicked: Int = com.jinproject.design_ui.R.drawable.icon_simulator
    }
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

fun NavController.navigateToSimulatorGraph(navOptions: NavOptions? = null) {
    navigate(SimulatorRoute.SimulatorGraph, navOptions)
}