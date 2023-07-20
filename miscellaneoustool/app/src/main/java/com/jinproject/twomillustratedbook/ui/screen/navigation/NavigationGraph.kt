package com.jinproject.twomillustratedbook.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jinproject.features.alarm.AlarmScreen
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.gear.GearScreen
import com.jinproject.twomillustratedbook.ui.screen.watch.WatchScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    showRewardedAd: (()->Unit) -> Unit,
    showSnackBar: suspend (SnackBarMessage) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Alarm.route
    ) {
        composable(route = NavigationItem.Alarm.route) {
            AlarmScreen(
                showRewardedAd = showRewardedAd,
                changeVisibilityBottomNavigationBar = changeVisibilityBottomNavigationBar,
                onNavigateToGear = { navController.navigate(NavigationItem.Gear.route) },
                onNavigateToWatch = { navController.navigate(NavigationItem.Watch.route) },
                showSnackBar = showSnackBar
            )
        }

        composable(route = NavigationItem.Gear.route) {
            GearScreen(
                changeVisibilityBottomNavigationBar = changeVisibilityBottomNavigationBar,
                onNavigatePopBackStack = { navController.popBackStack() },
                showSnackBar = showSnackBar
            )
        }

        composable(route = NavigationItem.Watch.route) {
            WatchScreen(
                changeVisibilityBottomNavigationBar = changeVisibilityBottomNavigationBar,
                onNavigatePopBackStack = { navController.popBackStack() }
            )
        }
    }
}