package com.jinproject.features.alarm

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.jinproject.features.alarm.alarm.AlarmScreen
import com.jinproject.features.alarm.setting.AlarmSettingScreen
import com.jinproject.features.core.BillingModule
import kotlinx.serialization.Serializable

@Serializable
internal data object AlarmSetting : AlarmRoute

fun EntryProviderScope<NavKey>.alarmEntries(
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
) {
    entry<AlarmRoute.Alarm> {
        AlarmScreen(
            billingModule = billingModule,
            showRewardedAd = showRewardedAd,
        )
    }

    entry<AlarmSetting>(
        metadata = NavDisplay.transitionSpec {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        } + NavDisplay.popTransitionSpec {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        } + NavDisplay.predictivePopTransitionSpec {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    ) { AlarmSettingScreen() }
}
