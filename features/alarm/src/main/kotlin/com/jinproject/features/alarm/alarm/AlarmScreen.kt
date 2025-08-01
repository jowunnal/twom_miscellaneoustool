package com.jinproject.features.alarm.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.TextDialog
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.rememberDialogState
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.alarm.component.AlarmBottomSheetContent
import com.jinproject.features.alarm.alarm.component.AlarmTopAppBar
import com.jinproject.features.alarm.alarm.component.BossSelectionItem
import com.jinproject.features.alarm.alarm.component.InProgressTimerItem
import com.jinproject.features.alarm.alarm.component.SearchBossContent
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    billingModule: BillingModule,
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    showRewardedAd: (() -> Unit) -> Unit,
    onNavigateToWatch: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit
) {

    val alarmUiState: AlarmUiState by alarmViewModel.uiState.collectAsStateWithLifecycle()

    if (alarmUiState.timerList.isNotEmpty()) {
        if (alarmUiState.timerList.first().bossName == "실패") {
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
        addBossToFrequentlyUsedList = alarmViewModel::addBossToFrequentlyUsedList,
        removeBossFromFrequentlyUsedList = alarmViewModel::removeBossFromFrequentlyUsedList,
        onStartAlarm = { bossName, deadTime ->
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val setAlarm: () -> Unit = {
                alarmViewModel.setAlarm(
                    monsterName = bossName,
                    deadTime = deadTime,
                    showSnackBar = showSnackBar,
                )
            }

            if (Build.VERSION.SDK_INT >= 31) {
                if (alarmManager.canScheduleExactAlarms())
                    coroutineScope.launch {
                        if (billingModule.isProductPurchased(BillingModule.Product.AD_REMOVE))
                            setAlarm()
                        else
                            showRewardedAd {
                                setAlarm()
                            }
                    }
                else
                    context.startActivity(
                        Intent(
                            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                            ("package:" + context.packageName).toUri()
                        )
                    )
            } else
                setAlarm()
        },
        onClearAlarm = alarmViewModel::clearAlarm,
        addOverlayMonster = alarmViewModel::addOverlayMonster,
        removeOverlayMonster = alarmViewModel::removeOverlayMonster,
        onNavigateToWatch = onNavigateToWatch
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun AlarmScreen(
    alarmUiState: AlarmUiState,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    focusManager: FocusManager = LocalFocusManager.current,
    addBossToFrequentlyUsedList: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    onStartAlarm: (monsterName: String, deadTime: ZonedDateTime) -> Unit,
    onClearAlarm: (Int, String) -> Unit,
    addOverlayMonster: (String) -> Unit,
    removeOverlayMonster: (String) -> Unit,
    onNavigateToWatch: () -> Unit,
) {
    var bottomSheetVisibility by remember {
        mutableStateOf(false)
    }

    var dialogUiState by rememberDialogState()
    var selectedMonster by remember {
        mutableStateOf("")
    }

    var transitionState by remember {
        mutableStateOf(false)
    }

    TextDialog(
        dialogState = dialogUiState,
        onDismissRequest = { dialogUiState.changeVisibility(false) }
    )

    if (bottomSheetVisibility)
        ModalBottomSheet(
            onDismissRequest = {
                bottomSheetVisibility = false
            },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(20.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_handle_bar),
                        contentDescription = "HandleBar",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_x),
                        contentDescription = "Exit Bottom sheet",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .height(48.dp)
                            .padding(top = 16.dp, end = 16.dp)
                            .clickableAvoidingDuplication {
                                bottomSheetVisibility = false
                            }
                    )
                }
            }
        ) {
            AlarmBottomSheetContent(
                selectedBossName = selectedMonster,
                overlaidBossList = alarmUiState.overlaidBossList,
                onStartAlarm = onStartAlarm,
                onCloseBottomSheet = { checkState ->
                    bottomSheetVisibility = false

                    if (checkState)
                        addOverlayMonster(selectedMonster)
                    else
                        removeOverlayMonster(selectedMonster)
                },
            )
        }

    DefaultLayout(
        modifier = Modifier.clickableAvoidingDuplication {
            focusManager.clearFocus()
            transitionState = false
        },
        topBar = {
            AlarmTopAppBar(
                onNavigateToOverlaySetting = onNavigateToWatch
            )
        },
        verticalScrollable = true,
        contentPaddingValues = MiscellanousToolPaddingValues(horizontal = 12.dp),
    ) {
        SharedTransitionLayout {
            SearchBossContent(
                bossNameList = alarmUiState.monsterList,
                transitionState = transitionState,
                sharedTransitionScope = this@SharedTransitionLayout,
                addBossToFrequentlyUsedList = addBossToFrequentlyUsedList,
                setTransitionState = { bool -> transitionState = bool }
            )
        }
        if (alarmUiState.frequentlyUsedBossList.isNotEmpty()) {
            HorizontalDivider()
            DescriptionLargeText(
                text = stringResource(id = R.string.watch_title_recently_bosslist),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .padding(vertical = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.Center
            ) {
                alarmUiState.frequentlyUsedBossList.forEach { item ->
                    key(item) {
                        BossSelectionItem(
                            bossName = item,
                            onClickBossItem = { bossName ->
                                selectedMonster = bossName
                                bottomSheetVisibility = true
                            },
                            removeBossFromFrequentlyUsedList = removeBossFromFrequentlyUsedList,
                            onOpenDialog = { state ->
                                dialogUiState = state.apply {
                                    changeVisibility(true)
                                }
                            },
                            onCloseDialog = { dialogUiState.changeVisibility(false) }
                        )
                    }
                }
            }
        }
        if (alarmUiState.timerList.isNotEmpty()) {
            HorizontalDivider()
            VerticalSpacer(height = 20.dp)
            DescriptionLargeText(
                text = stringResource(id = R.string.alarm_present_bosslist),
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
            VerticalSpacer(height = 20.dp)

            alarmUiState.timerList.forEach { timer ->
                key(timer.id) {
                    InProgressTimerItem(
                        timerState = timer,
                        onClearAlarm = onClearAlarm,
                        onOpenDialog = { state ->
                            dialogUiState = state.apply {
                                changeVisibility(true)
                            }
                        },
                        onCloseDialog = { dialogUiState.changeVisibility(false) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PreviewAlarmScreen(
    @PreviewParameter(AlarmUiStatePreviewParameter::class)
    alarmUiState: AlarmUiState,
) {
    PreviewMiscellaneousToolTheme {
        AlarmScreen(
            alarmUiState = alarmUiState,
            addBossToFrequentlyUsedList = {},
            removeBossFromFrequentlyUsedList = {},
            onStartAlarm = { _, _ -> },
            onClearAlarm = { _, _ -> },
            addOverlayMonster = {},
            removeOverlayMonster = {},
            onNavigateToWatch = {},
        )
    }
}