package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jinproject.twomillustratedbook.ui.screen.alarm.AlarmScreen
import com.jinproject.twomillustratedbook.ui.screen.alarm.AlarmViewModel
import com.jinproject.twomillustratedbook.ui.screen.alarm.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.DropListViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    dropListViewModel: DropListViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Alarm.route
    ) {
        composable(route = NavigationItem.Alarm.route) {
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
                setRecentlySelectedBossNameChanged = alarmViewModel::setRecentlySelectedBossName
            )
        }
    }
}