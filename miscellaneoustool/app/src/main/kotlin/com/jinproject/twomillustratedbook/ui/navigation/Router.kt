package com.jinproject.twomillustratedbook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.jinproject.features.alarm.AlarmRoute
import com.jinproject.features.alarm.navigateToAlarmGraph
import com.jinproject.features.core.compose.TopLevelRoute
import com.jinproject.features.home.HomeRoute
import com.jinproject.features.home.navigateToHomeGraph
import com.jinproject.features.simulator.SimulatorRoute
import com.jinproject.features.simulator.navigateToSimulatorGraph
import com.jinproject.features.symbol.SymbolRoute
import com.jinproject.features.symbol.navigateToSymbolGraph

internal val TopLevelRoutes: List<TopLevelRoute> = listOf(
    HomeRoute.Home,
    SimulatorRoute.Simulator,
    SymbolRoute.Symbol,
    AlarmRoute.Alarm,
)

/**
 * Navigation을 담당하는 클래스
 * @param navController navigation을 수행하는 주체
 */
@Stable
internal class Router(val navController: NavHostController) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    internal fun navigateTopLevelDestination(topLevelRoute: TopLevelRoute) {
        val navOptions = navOptions {
            navController.currentBackStackEntry?.destination?.route?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
            launchSingleTop = true
        }

        when (topLevelRoute) {
            is AlarmRoute.Alarm -> navController.navigateToAlarmGraph(navOptions)
            is HomeRoute.Home -> navController.navigateToHomeGraph(navOptions)
            is SimulatorRoute.Simulator -> navController.navigateToSimulatorGraph(navOptions)
            is SymbolRoute.Symbol -> navController.navigateToSymbolGraph(navOptions)
        }
    }

}

fun NavDestination?.isBarHasToBeShown(): Boolean =
    this?.let {
        TopLevelRoutes.any { topLevelRoute -> hasRoute(route = topLevelRoute::class) }
    } ?: false

fun <T> NavDestination?.isDestinationInHierarchy(destination: T) =
    this?.hierarchy?.any {
        it.hasRoute(destination!!::class)
    } ?: false

fun NavController.popBackStackIfCan() {
    this.previousBackStackEntry?.let {
        this.popBackStack()
    }
}