package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.alarm.AlarmScreen
import com.jinproject.twomillustratedbook.ui.screen.alarm.AlarmViewModel
import com.jinproject.twomillustratedbook.ui.screen.gear.GearScreen
import com.jinproject.twomillustratedbook.ui.screen.gear.GearViewModel
import com.jinproject.twomillustratedbook.ui.screen.watch.WatchScreen
import com.jinproject.twomillustratedbook.ui.screen.watch.WatchViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    showRewardedAd: () -> Unit,
    checkAuthorityDrawOverlays:(Context, (Intent)->Unit) -> Boolean,
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    gearViewModel: GearViewModel = hiltViewModel(),
    watchViewModel: WatchViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Alarm.route
    ) {
        composable(route = NavigationItem.Alarm.route) {
            changeVisibilityBottomNavigationBar(true)

            val alarmUiState by alarmViewModel.uiState.collectAsStateWithLifecycle()
            val snackBarMessage by alarmViewModel.snackBarMessage.collectAsStateWithLifecycle(
                initialValue = SnackBarMessage.getInitValues(),
                lifecycleOwner = LocalLifecycleOwner.current
            )

            AlarmScreen(
                alarmUiState = alarmUiState,
                snackBarMessage = snackBarMessage,
                addBossToFrequentlyUsedList = alarmViewModel::addBossToFrequentlyUsedList,
                removeBossFromFrequentlyUsedList = alarmViewModel::removeBossFromFrequentlyUsedList,
                onStartAlarm = alarmViewModel::setAlarm,
                onClearAlarm = alarmViewModel::clearAlarm,
                setHourChanged = alarmViewModel::setHourChanged,
                setMinutesChanged = alarmViewModel::setMinutesChanged,
                setSecondsChanged = alarmViewModel::setSecondsChanged,
                setSelectedBossName = alarmViewModel::setSelectedBossName,
                setRecentlySelectedBossClassifiedChanged = alarmViewModel::setRecentlySelectedBossClassified,
                setRecentlySelectedBossNameChanged = alarmViewModel::setRecentlySelectedBossName,
                onNavigateToGear = {
                    navController.navigate(NavigationItem.Gear.route)
                },
                onNavigateToWatch = {
                    navController.navigate(NavigationItem.Watch.route)
                },
                showRewardedAd = showRewardedAd
            )
        }

        composable(route = NavigationItem.Gear.route) {
            changeVisibilityBottomNavigationBar(false)

            val gearUiState by gearViewModel.uiState.collectAsStateWithLifecycle()
            val snackBarMessage by gearViewModel.snackBarMessage.collectAsStateWithLifecycle(
                initialValue = SnackBarMessage.getInitValues(),
                lifecycleOwner = LocalLifecycleOwner.current
            )

            GearScreen(
                gearUiState = gearUiState,
                snackBarMessage = snackBarMessage,
                setIntervalFirstTimerSetting = gearViewModel::setIntervalFirstTimerSetting,
                setIntervalSecondTimerSetting = gearViewModel::setIntervalSecondTimerSetting,
                onNavigatePopBackStack = { navController.popBackStack() },
                emitSnackBar = gearViewModel::emitSnackBar
            )
        }

        composable(route = NavigationItem.Watch.route) {
            changeVisibilityBottomNavigationBar(false)

            val context = LocalContext.current
            val uiState by watchViewModel.uiState.collectAsStateWithLifecycle()

            WatchScreen(
                uiState = uiState,
                activityContext = context,
                setWatchStatus = watchViewModel::setWatchStatus,
                setFontSize = watchViewModel::setFontSize,
                checkAuthorityDrawOverlays = checkAuthorityDrawOverlays,
                onNavigatePopBackStack = { navController.popBackStack() },
            )
        }
    }
}