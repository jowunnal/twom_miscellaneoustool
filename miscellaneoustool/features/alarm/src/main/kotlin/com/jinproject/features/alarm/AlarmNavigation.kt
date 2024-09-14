package com.jinproject.features.alarm

import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jinproject.features.alarm.alarm.AlarmScreen
import com.jinproject.features.alarm.gear.GearScreen
import com.jinproject.features.alarm.watch.WatchScreen
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.Route
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed class AlarmRoute: Route {
    @Serializable
    data object AlarmGraph: AlarmRoute()

    @Serializable
    data object Alarm : AlarmRoute()

    @Serializable
    data object Gear : AlarmRoute()

    @Serializable
    data object Watch : AlarmRoute()
}

fun NavGraphBuilder.alarmNavGraph(
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
    onNavigateToGear: () -> Unit,
    onNavigateToWatch: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    popBackStackIfCan: () -> Unit,
) {
    navigation<AlarmRoute.AlarmGraph>(
        startDestination = AlarmRoute.Alarm,
    ) {
        composable<AlarmRoute.Alarm>(
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
            AlarmScreen(
                billingModule = billingModule,
                backToAlarmIntent = Intent(
                    LocalContext.current,
                    Class.forName("com.jinproject.twomillustratedbook.ui.MainActivity")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("screen", "alarm")
                },
                showRewardedAd = showRewardedAd,
                onNavigateToGear = onNavigateToGear,
                onNavigateToWatch = onNavigateToWatch,
                showSnackBar = showSnackBar
            )
        }

        composable<AlarmRoute.Gear>(
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
            GearScreen(
                billingModule = billingModule,
                onNavigatePopBackStack = popBackStackIfCan,
                showSnackBar = showSnackBar
            )
        }

        composable<AlarmRoute.Watch>(
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
            WatchScreen(
                onNavigatePopBackStack = popBackStackIfCan,
            )
        }
    }
}

fun NavController.navigateToAlarm(navOptions: NavOptions?) {
    this.navigate(AlarmRoute.Alarm, navOptions)
}

fun NavController.navigateToGear() {
    this.navigate(AlarmRoute.Gear)
}

fun NavController.navigateToWatch() {
    this.navigate(AlarmRoute.Watch)
}