package com.jinproject.twomillustratedbook.ui.screen.navigation

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.features.alarm.alarmNavGraph
import com.jinproject.features.alarm.navigateToGear
import com.jinproject.features.alarm.navigateToWatch
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.Route
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.findActivity
import com.jinproject.features.droplist.dropListNavigation
import com.jinproject.features.simulator.simulatorNavGraph
import com.jinproject.features.symbol.symbolNavGraph
import com.jinproject.twomillustratedbook.R

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    navHostController: NavHostController,
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    startDestination: Route,
) {
    val bottomBar = remember {
        context.findActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }
    val router = remember(navHostController) { Router(navHostController) }
    val navController = router.navController

    LaunchedEffect(key1 = router.currentDestination) {
        bottomBar.visibility =
            if (navController.currentDestination.isBarHasToBeShown()) View.VISIBLE else View.GONE
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        alarmNavGraph(
            billingModule = billingModule,
            showRewardedAd = showRewardedAd,
            onNavigateToGear = navController::navigateToGear,
            onNavigateToWatch = navController::navigateToWatch,
            showSnackBar = showSnackBar,
            popBackStackIfCan = navController::popBackStackIfCan,
        )

        symbolNavGraph(
            billingModule = billingModule,
            navController = navController,
            showSnackBar = showSnackBar,
            setBottomBarVisibility = { visibility ->
                bottomBar.visibility = visibility
            }
        )

        simulatorNavGraph()

        dropListNavigation(
            setBottomBarVisibility = { visibility ->
                bottomBar.visibility = visibility
            }
        )
    }
}