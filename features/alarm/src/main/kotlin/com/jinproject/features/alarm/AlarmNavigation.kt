package com.jinproject.features.alarm

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.jinproject.features.alarm.alarm.AlarmScreen
import com.jinproject.features.alarm.setting.AlarmSettingScreen
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.Route
import com.jinproject.features.core.compose.TopLevelRoute
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed class AlarmRoute : Route {
    @Serializable
    data object AlarmGraph : AlarmRoute()

    @Serializable
    data object Alarm : AlarmRoute(), TopLevelRoute {
        override val icon: Int = com.jinproject.design_ui.R.drawable.icon_alarm
        override val iconClicked: Int = com.jinproject.design_ui.R.drawable.icon_alarm
    }

    @Serializable
    data object Setting : AlarmRoute()
}

fun NavGraphBuilder.alarmNavGraph(
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
    onNavigateToAlarmSetting: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    popBackStackIfCan: () -> Unit,
) {
    navigation<AlarmRoute.AlarmGraph>(
        startDestination = AlarmRoute.Alarm,
        deepLinks = listOf(
            navDeepLink<AlarmRoute.AlarmGraph>(
                basePath = "twom_miscellanous_tool/alarm"
            )
        ),
    ) {
        composable<AlarmRoute.Alarm> {
            AlarmScreen(
                billingModule = billingModule,
                showRewardedAd = showRewardedAd,
                onNavigateToWatch = onNavigateToAlarmSetting,
                showSnackBar = showSnackBar,
            )
        }

        composable<AlarmRoute.Setting>(
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
            AlarmSettingScreen(
                onNavigatePopBackStack = popBackStackIfCan,
            )
        }
    }
}

fun NavController.navigateToAlarmGraph(navOptions: NavOptions?) {
    this.navigate(AlarmRoute.AlarmGraph, navOptions)
}

fun NavController.navigateToAlarmSetting() {
    this.navigate(AlarmRoute.Setting)
}