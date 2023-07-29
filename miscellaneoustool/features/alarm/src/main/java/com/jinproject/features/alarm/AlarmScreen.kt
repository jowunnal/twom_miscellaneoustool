package com.jinproject.features.alarm

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.DialogCustom
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.domain.model.WeekModel
import com.jinproject.features.alarm.component.AlarmBottomSheetContent
import com.jinproject.features.alarm.component.AlarmTopAppBar
import com.jinproject.features.alarm.component.BossSelection
import com.jinproject.features.alarm.component.InProgressTimerList
import com.jinproject.features.alarm.item.TimeState
import com.jinproject.features.alarm.item.TimerState
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AlarmScreen(
    billingModule: BillingModule,
    backToAlarmIntent: Intent,
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    showRewardedAd: (() -> Unit) -> Unit,
    onNavigateToGear: () -> Unit,
    onNavigateToWatch: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit
) {
    changeVisibilityBottomNavigationBar(true)

    val alarmUiState by alarmViewModel.uiState.collectAsStateWithLifecycle()
    val alarmBottomSheetUiState by alarmViewModel.bottomSheetUiState.collectAsStateWithLifecycle()

    if(alarmUiState.timerList.isNotEmpty()) {
        if(alarmUiState.timerList.first().bossName == "실패") {
            showSnackBar(
                SnackBarMessage(
                    headerMessage = "데이터를 불러오는데 실패했어요.",
                    contentMessage = "업데이트가 아닌 삭제후 설치를 진행해주세요."
                )
            )
        }
    }

    AlarmScreen(
        alarmUiState = alarmUiState,
        alarmBottomSheetUiState = alarmBottomSheetUiState,
        addBossToFrequentlyUsedList = alarmViewModel::addBossToFrequentlyUsedList,
        removeBossFromFrequentlyUsedList = { bossName ->
            alarmViewModel::removeBossFromFrequentlyUsedList.invoke(bossName, showSnackBar)
        },
        onStartAlarm = { bossName ->
            if (Build.VERSION.SDK_INT >= 31) {
                val alarmManager = context.getSystemService<AlarmManager>()!!
                when {
                    alarmManager.canScheduleExactAlarms() -> {
                        billingModule.queryPurchase { purchaseList ->
                            coroutineScope.launch(Dispatchers.Main) {
                                if (billingModule.checkPurchased(
                                        purchaseList = purchaseList,
                                        productId = "ad_remove"
                                    )
                                ) {
                                    showRewardedAd {
                                        alarmViewModel::setAlarm.invoke(bossName, showSnackBar, backToAlarmIntent)
                                    }
                                } else
                                    alarmViewModel::setAlarm.invoke(bossName, showSnackBar, backToAlarmIntent)
                            }
                        }
                    }

                    else -> {
                        context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            } else {
                alarmViewModel::setAlarm.invoke(bossName, showSnackBar, backToAlarmIntent)
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
        onNavigateToWatch = onNavigateToWatch
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlarmScreen(
    alarmUiState: AlarmUiState,
    alarmBottomSheetUiState: AlarmBottomSheetUiState,
    addBossToFrequentlyUsedList: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
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
    onNavigateToWatch: () -> Unit
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

    DefaultLayout(
        topBar = {
            AlarmTopAppBar(
                onNavigateToGear = onNavigateToGear,
                onNavigateToOverlaySetting = onNavigateToWatch
            )
        }
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
                    }
                )
            },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
            onNavigateToWatch = {}
        )
    }
}