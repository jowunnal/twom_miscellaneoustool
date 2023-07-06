package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jinproject.twomillustratedbook.ui.screen.alarm.AlarmScreen
import com.jinproject.twomillustratedbook.ui.screen.alarm.AlarmViewModel
import com.jinproject.twomillustratedbook.ui.screen.gear.GearScreen
import com.jinproject.twomillustratedbook.ui.screen.gear.GearViewModel
import com.jinproject.twomillustratedbook.ui.screen.watch.WatchScreen
import com.jinproject.twomillustratedbook.ui.screen.watch.WatchViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    billingModule: BillingModule,
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    showRewardedAd: (()->Unit) -> Unit,
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    gearViewModel: GearViewModel = hiltViewModel(),
    watchViewModel: WatchViewModel = hiltViewModel()
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
                onNavigateToWatch = { navController.navigate(NavigationItem.Watch.route) }
            )
        }

        composable(route = NavigationItem.Gear.route) {
            GearScreen(
                changeVisibilityBottomNavigationBar = changeVisibilityBottomNavigationBar,
                billingModule = billingModule,
                gearViewModel = gearViewModel,
                onNavigatePopBackStack = { navController.popBackStack() }
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