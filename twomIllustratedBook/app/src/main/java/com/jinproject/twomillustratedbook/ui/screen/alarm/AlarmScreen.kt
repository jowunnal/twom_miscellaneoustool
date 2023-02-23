package com.jinproject.twomillustratedbook.ui.screen.alarm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.twomillustratedbook.domain.model.MonsterType
import com.jinproject.twomillustratedbook.domain.model.WeekModel
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.alarm.component.*
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultLayout
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalDivider
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.mate.carpool.ui.composable.DialogCustom
import com.mate.carpool.ui.composable.DialogState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlarmScreen(
    alarmUiState: AlarmUiState,
    snackBarMessage: SnackBarMessage,
    addBossToFrequentlyUsedList: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    onStartAlarm: (String) -> Unit,
    onClearAlarm: (Int, String) -> Unit,
    setHourChanged: (Int) -> Unit,
    setMinutesChanged: (Int) -> Unit,
    setSecondsChanged: (Int) -> Unit,
    setSelectedBossName: (String) -> Unit,
    setRecentlySelectedBossClassifiedChanged: (MonsterType) -> Unit,
    setRecentlySelectedBossNameChanged: (String) -> Unit,
    onNavigateToGear: () -> Unit,
    onNavigateToWatch: () -> Unit,
    showRewardedAd: () -> Unit
) {
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
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

    if (snackBarMessage.headerMessage.isNotBlank())
        LaunchedEffect(key1 = snackBarMessage.headerMessage) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = snackBarMessage.headerMessage,
                    actionLabel = snackBarMessage.contentMessage,
                    duration = SnackbarDuration.Indefinite
                )
            }
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
                    timeState = alarmUiState.timeState,
                    selectedBossName = alarmUiState.selectedBossName,
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
    TwomIllustratedBookPreview {
        AlarmScreen(
            alarmUiState = AlarmUiState(
                timerList = listOf(
                    TimerState(
                        id = 1,
                        bossName = "보스1",
                        timeState = TimeState(
                            day = WeekModel.Mon,
                            hour = 14,
                            minutes = 22,
                            seconds = 25
                        )
                    ),
                    TimerState(
                        id = 2,
                        bossName = "보스2",
                        timeState = TimeState(
                            day = WeekModel.Mon,
                            hour = 16,
                            minutes = 18,
                            seconds = 33
                        )
                    ),
                    TimerState(
                        id = 3,
                        bossName = "보스3",
                        timeState = TimeState(
                            day = WeekModel.Mon,
                            hour = 13,
                            minutes = 34,
                            seconds = 49
                        )
                    )
                ),
                selectedBossName = "은둔자",
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
                ),
                timeState = TimeState.getInitValue()
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