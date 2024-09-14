package com.jinproject.twomillustratedbook.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.jinproject.features.alarm.AlarmRoute
import com.jinproject.features.alarm.navigateToAlarm
import com.jinproject.features.simulator.SimulatorRoute
import com.jinproject.features.symbol.SymbolRoute

data class  TopLevelRoute<T>(val route: T)

internal val TopLevelRoutes = listOf(
    TopLevelRoute(route = AlarmRoute.Alarm),
    TopLevelRoute(route = SymbolRoute.Symbol),
    TopLevelRoute(route = SimulatorRoute.Simulator),
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

    internal fun <T> navigateTopLevelDestination(destination: T) {
        val navOptions = navOptions {
            navController.currentBackStackEntry?.destination?.route?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
            launchSingleTop = true
        }

        when (destination) {
            is AlarmRoute.Alarm -> {
                navController.navigateToAlarm(navOptions)
            }

            is SimulatorRoute.Simulator -> {

            }

            else -> throw IllegalStateException("$destination is not allowed to navigate")
        }
    }

}

fun NavDestination?.isBarHasToBeShown(): Boolean =
    this?.let {
        TopLevelRoutes.any { topLevelRoute -> hasRoute(route = topLevelRoute.route::class) }
    } ?: false

fun NavController.popBackStackIfCan() {
    this.previousBackStackEntry?.let {
        this.popBackStack()
    }
}