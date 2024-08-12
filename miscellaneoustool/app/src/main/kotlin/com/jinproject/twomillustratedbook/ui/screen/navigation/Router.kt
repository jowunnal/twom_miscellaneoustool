package com.jinproject.twomillustratedbook.ui.screen.navigation

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions

/**
 * Navigation을 담당하는 클래스
 * @param navController navigation을 수행하는 주체
 */
@Stable
internal class Router(val navController: NavHostController, val bottomBar: View) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    internal fun navigate(destination: ComposeNavigationDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (destination) {
            ComposeNavigationDestination.Alarm -> {
                bottomBar.visibility = View.VISIBLE
                navController.navigateToAlarm(navOptions)
            }

            ComposeNavigationDestination.Gear -> {
                bottomBar.visibility = View.GONE
                navController.navigateToGear(null)
            }

            ComposeNavigationDestination.Watch -> {
                bottomBar.visibility = View.GONE
                navController.navigateToWatch(null)
            }
        }
    }

}

fun NavDestination?.isAlarmRoute(): Boolean =
    (this?.route ?: AlarmRoute) == AlarmRoute

fun NavDestination?.isDestinationInHierarchy(destination: ComposeNavigationDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false

fun NavController.popBackStackIfCan() {
    this.previousBackStackEntry?.let {
        this.popBackStack()
    }
}

fun NavController.navigateToAlarm(navOptions: NavOptions?) {
    this.navigate(AlarmRoute, navOptions)
}

fun NavController.navigateToGear(navOptions: NavOptions?) {
    this.navigate(GearRoute, navOptions)
}

fun NavController.navigateToWatch(navOptions: NavOptions?) {
    this.navigate(WatchRoute, navOptions)
}