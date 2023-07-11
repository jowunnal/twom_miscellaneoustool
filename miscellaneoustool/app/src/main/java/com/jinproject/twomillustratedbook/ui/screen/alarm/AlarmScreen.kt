package com.jinproject.twomillustratedbook.ui.screen.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.alarm.component.AlarmBottomSheetContent
import com.jinproject.twomillustratedbook.ui.screen.alarm.component.AlarmTopAppBar
import com.jinproject.twomillustratedbook.ui.screen.alarm.component.BossSelection
import com.jinproject.twomillustratedbook.ui.screen.alarm.component.InProgressTimerList
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultLayout
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DialogCustom
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DialogState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalDivider
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.utils.PreviewMiscellaneousToolTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AlarmScreen(
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    showRewardedAd: (() -> Unit) -> Unit,
    onNavigateToGear: () -> Unit,
    onNavigateToWatch: () -> Unit
) {
    changeVisibilityBottomNavigationBar(true)

    val alarmUiState by alarmViewModel.uiState.collectAsStateWithLifecycle()
    val alarmBottomSheetUiState by alarmViewModel.bottomSheetUiState.collectAsStateWithLifecycle()
    val snackBarMessage by alarmViewModel.snackBarMessage.collectAsStateWithLifecycle(
        initialValue = SnackBarMessage.getInitValues(),
        lifecycleOwner = LocalLifecycleOwner.current
    )

    AlarmScreen(
        alarmUiState = alarmUiState,
        alarmBottomSheetUiState = alarmBottomSheetUiState,
        snackBarMessage = snackBarMessage,
        addBossToFrequentlyUsedList = alarmViewModel::addBossToFrequentlyUsedList,
        removeBossFromFrequentlyUsedList = alarmViewModel::removeBossFromFrequentlyUsedList,
        onStartAlarm = { bossName ->
            if (Build.VERSION.SDK_INT >= 31) {
                val alarmManager = context.getSystemService<AlarmManager>()!!
                when {
                    alarmManager.canScheduleExactAlarms() -> {
                        alarmViewModel::setAlarm.invoke(bossName)
                    }

                    else -> {
                        context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            } else {
                alarmViewModel::setAlarm.invoke(bossName)
            }
        },
        onClearAlarm = alarmViewModel::clearAlarm,
        setHourChanged = alarmViewModel::setHourChanged,
        setMinutesChanged = alarmViewModel::setMinutesChanged,
        setSecondsChanged = alarmViewModel::setSecondsChanged,
        setSelectedBossName = alarmViewModel::setSelectedBossName,
        setRecentlySelectedBossClassifiedChanged = alarmViewModel::setRecentlySelectedBossClassified,
        setRecentlySelectedBossNameChanged = alarmViewModel::setRecentlySelectedBossName,
        onNavigateToGear = onNavigateToGear,
        onNavigateToWatch = onNavigateToWatch,
        showRewardedAd = showRewardedAd
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlarmScreen(
    alarmUiState: AlarmUiState,
    alarmBottomSheetUiState: AlarmBottomSheetUiState,
    snackBarMessage: SnackBarMessage,
    addBossToFrequentlyUsedList: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onStartAlarm: (String) -> Unit,
    onClearAlarm: (Int, String) -> Unit,
    setHourChanged: (Int) -> Unit,
    setMinutesChanged: (Int) -> Unit,
    setSecondsChanged: (Int) -> Unit,
    setSelectedBossName: (String) -> Unit,
    setRecentlySelectedBossClassifiedChanged: (com.jinproject.domain.model.MonsterType) -> Unit,
    setRecentlySelectedBossNameChanged: (String) -> Unit,
    onNavigateToGear: () -> Unit,
    onNavigateToWatch: () -> Unit,
    showRewardedAd: (() -> Unit) -> Unit
) {
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val showDialogState = remember {
        mutableStateOf(false)
    }
    val dialogUiState = remember {
        mutableStateOf(DialogState.getInitValue())
    }

    if (showDialogState.value)
        DialogCustom(
            dialogState = dialogUiState.value,
            onDismissRequest = { showDialogState.value = false }
        )


    LaunchedEffect(key1 = snackBarMessage) {
        if (snackBarMessage.headerMessage.isNotBlank())
            scaffoldState.snackbarHostState.showSnackbar(
                message = snackBarMessage.headerMessage,
                actionLabel = snackBarMessage.contentMessage,
                duration = SnackbarDuration.Indefinite
            )
    }

    DefaultLayout(
        topBar = {
            AlarmTopAppBar(
                onNavigateToGear = onNavigateToGear,
                onNavigateToOverlaySetting = onNavigateToWatch
            )
        },
        scaffoldState = scaffoldState
    ) {
        ModalBottomSheetLayout(
            sheetContent = {
                AlarmBottomSheetContent(
                    timeState = alarmBottomSheetUiState.timeState,
                    selectedBossName = alarmBottomSheetUiState.selectedBossName,
                    setHourChanged = setHourChanged,
                    setMinutesChanged = setMinutesChanged,
                    setSecondsChanged = setSecondsChanged,
                    onStartAlarm = onStartAlarm,
                    onCloseBottomSheet = {
                        coroutineScope.launch {
                            bottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    showRewardedAd = showRewardedAd
                )
            },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
            ) {
                BossSelection(
                    bossNameList = alarmUiState.bossNameList,
                    recentlySelectedBossClassified = alarmUiState.recentlySelectedBossClassified,
                    recentlySelectedBossName = alarmUiState.recentlySelectedBossName,
                    frequentlyUsedBossList = alarmUiState.frequentlyUsedBossList,
                    onClickBossItem = { bossName ->
                        coroutineScope.launch {
                            setSelectedBossName(bossName)
                            bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    addBossToFrequentlyUsedList = addBossToFrequentlyUsedList,
                    removeBossFromFrequentlyUsedList = removeBossFromFrequentlyUsedList,
                    setRecentlySelectedBossClassifiedChanged = setRecentlySelectedBossClassifiedChanged,
                    setRecentlySelectedBossNameChanged = setRecentlySelectedBossNameChanged,
                    onOpenDialog = { dialogState ->
                        dialogUiState.value = dialogState
                        showDialogState.value = true
                    },
                    onCloseDialog = { showDialogState.value = false }
                )
                VerticalSpacer(height = 20.dp)
                HorizontalDivider()
                InProgressTimerList(
                    timerStateList = alarmUiState.timerList,
                    onClearAlarm = onClearAlarm,
                    onOpenDialog = { dialogState ->
                        dialogUiState.value = dialogState
                        showDialogState.value = true
                    },
                    onCloseDialog = { showDialogState.value = false }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmScreen() {
    PreviewMiscellaneousToolTheme {
        AlarmScreen(
            alarmUiState = AlarmUiState(
                timerList = listOf(
                    TimerState(
                        id = 1,
                        bossName = "보스1",
                        timeState = TimeState(
                            day = com.jinproject.domain.model.WeekModel.Mon,
                            hour = 14,
                            minutes = 22,
                            seconds = 25
                        )
                    ),
                    TimerState(
                        id = 2,
                        bossName = "보스2",
                        timeState = TimeState(
                            day = com.jinproject.domain.model.WeekModel.Mon,
                            hour = 16,
                            minutes = 18,
                            seconds = 33
                        )
                    ),
                    TimerState(
                        id = 3,
                        bossName = "보스3",
                        timeState = TimeState(
                            day = com.jinproject.domain.model.WeekModel.Mon,
                            hour = 13,
                            minutes = 34,
                            seconds = 49
                        )
                    )
                ),
                recentlySelectedBossClassified = "보스",
                recentlySelectedBossName = "바슬라프",
                bossNameList = listOf(
                    "은둔자",
                    "와당카",
                    "빅마마",
                    "바슬라프",
                    "아이요의수호병",
                    "칼리고",
                    "데블랑",
                    "우크파나"
                ),
                frequentlyUsedBossList = listOf(
                    "은둔자",
                    "와당카",
                    "빅마마",
                    "바슬라프",
                    "아이요의수호병",
                    "칼리고",
                    "데블랑",
                    "우크파나"
                )
            ),
            alarmBottomSheetUiState = AlarmBottomSheetUiState(
                timeState = TimeState.getInitValue(),
                selectedBossName = "은둔자",
            ),
            snackBarMessage = SnackBarMessage.getInitValues(),
            setSelectedBossName = {},
            addBossToFrequentlyUsedList = {},
            removeBossFromFrequentlyUsedList = {},
            onStartAlarm = {},
            onClearAlarm = { _, _ -> },
            setHourChanged = {},
            setMinutesChanged = {},
            setSecondsChanged = {},
            setRecentlySelectedBossClassifiedChanged = {},
            setRecentlySelectedBossNameChanged = {},
            onNavigateToGear = {},
            onNavigateToWatch = {},
            showRewardedAd = {}
        )
    }
}