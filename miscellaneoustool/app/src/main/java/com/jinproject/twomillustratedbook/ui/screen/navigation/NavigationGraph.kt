package com.jinproject.twomillustratedbook.ui.screen.navigation

import android.content.Intent
import android.view.View
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.features.alarm.AlarmScreen
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.findActivity
import com.jinproject.features.gear.GearScreen
import com.jinproject.features.symbol.symbolNavGraph
import com.jinproject.features.watch.WatchScreen
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.MainActivity

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    startDestination: String,
    navHostController: NavHostController,
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val context = LocalContext.current
    val bottomBar = context.findActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    val router = remember(navHostController) { Router(navHostController, bottomBar) }
    val navController = router.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = ComposeNavigationDestination.Alarm.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            }
        ) {
            bottomBar.visibility = View.VISIBLE
            AlarmScreen(
                billingModule = billingModule,
                backToAlarmIntent = Intent(LocalContext.current, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("screen", "alarm")
                },
                showRewardedAd = showRewardedAd,
                onNavigateToGear = { router::navigate.invoke(ComposeNavigationDestination.Gear) },
                onNavigateToWatch = { router::navigate.invoke(ComposeNavigationDestination.Watch) },
                showSnackBar = showSnackBar
            )
        }

        composable(
            route = ComposeNavigationDestination.Gear.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            }
        ) {
            bottomBar.visibility = View.GONE
            GearScreen(
                billingModule = billingModule,
                onNavigatePopBackStack = navController::popBackStackIfCan,
                showSnackBar = showSnackBar
            )
        }

        composable(
            route = ComposeNavigationDestination.Watch.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            }
        ) {
            bottomBar.visibility = View.GONE
            WatchScreen(
                onNavigatePopBackStack = navController::popBackStackIfCan
            )
        }

        symbolNavGraph(
            billingModule = billingModule,
            navController = navController,
            showSnackBar = showSnackBar,
            setBottomBarVisibility = { visibility ->
                bottomBar.visibility = visibility
            }
        )
    }
}