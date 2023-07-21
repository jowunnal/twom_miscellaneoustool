package com.jinproject.twomillustratedbook.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jinproject.features.alarm.AlarmScreen
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.gear.GearScreen
import com.jinproject.features.watch.WatchScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    billingModule: BillingModule,
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    showRewardedAd: (()->Unit) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Alarm.route,
        modifier = modifier
    ) {
        composable(route = NavigationItem.Alarm.route) {
            AlarmScreen(
                billingModule = billingModule,
                showRewardedAd = showRewardedAd,
                changeVisibilityBottomNavigationBar = changeVisibilityBottomNavigationBar,
                onNavigateToGear = { navController.navigate(NavigationItem.Gear.route) },
                onNavigateToWatch = { navController.navigate(NavigationItem.Watch.route) },
                showSnackBar = showSnackBar
            )
        }

        composable(route = NavigationItem.Gear.route) {
            GearScreen(
                billingModule = billingModule,
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