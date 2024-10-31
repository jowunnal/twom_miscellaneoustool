package com.jinproject.features.alarm.watch

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.ButtonStatus
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.theme.Typography
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.watch.component.OverlaySetting
import com.jinproject.features.alarm.watch.component.TimeStatusSetting
import com.jinproject.features.alarm.watch.component.TimerBottomSheetContent
import com.jinproject.features.alarm.watch.service.OverlayService
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WatchScreen(
    watchViewModel: WatchViewModel = hiltViewModel(),
    onNavigatePopBackStack: () -> Unit
) {
    val uiState by watchViewModel.uiState.collectAsStateWithLifecycle()

    WatchScreen(
        uiState = uiState,
        setWatchStatus = watchViewModel::setWatchStatus,
        setFontSize = watchViewModel::setFontSize,
        setXPos = watchViewModel::setXPos,
        setYPos = watchViewModel::setYPos,
        setTimerSetting = watchViewModel::setTimerSetting,
        onNavigatePopBackStack = onNavigatePopBackStack,
        setSelectedMonsterName = watchViewModel::setSelectedMonsterName,
        setSelectedMonsterOtaToTrue = watchViewModel::setSelectedMonsterOtaToTrue
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WatchScreen(
    uiState: WatchUiState,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    setWatchStatus: (ButtonStatus) -> Unit,
    setFontSize: (Int) -> Unit,
    setXPos: (Int) -> Unit,
    setYPos: (Int) -> Unit,
    setTimerSetting: () -> Unit,
    setSelectedMonsterName: (String) -> Unit,
    onNavigatePopBackStack: () -> Unit,
    setSelectedMonsterOtaToTrue: (Int) -> Unit
) {
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scrollState = rememberScrollState()
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    DefaultLayout(
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = onNavigatePopBackStack,
                title = stringResource(id = R.string.watch_appbar_title),
            )
        }
    ) {
        ModalBottomSheetLayout(
            sheetContent = {
                TimerBottomSheetContent(
                    selectedMonsterName = uiState.selectedMonsterName,
                    onCloseBottomSheet = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    setSelectedMonsterOtaToTrue = setSelectedMonsterOtaToTrue
                )
            },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(scrollState)
            ) {
                TimeStatusSetting(
                    watchStatus = uiState.watchStatus,
                    setWatchStatus = setWatchStatus,
                    startOverlayService = { status ->
                        context.startOverlayService(
                            status = status
                        )
                        localAnalyticsLoggingEvent(AnalyticsEvent.ASCT)
                    }
                )

                VerticalSpacer(height = 60.dp)

                OverlaySetting(
                    fontSize = uiState.fontSize,
                    xPos = uiState.xPos,
                    yPos = uiState.yPos,
                    setFontSize = setFontSize,
                    setXPos = setXPos,
                    setYPos = setYPos
                )
                VerticalSpacer(height = 16.dp)
                TextButton(
                    text = stringResource(id = R.string.set_do),
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        setTimerSetting()
                    }
                )

                VerticalSpacer(height = 60.dp)

                Text(
                    text = stringResource(id = R.string.watch_title_recently_bosslist),
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
                VerticalSpacer(height = 16.dp)

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.Center
                ) {
                    uiState.frequentlyUsedBossList.forEach { monsName ->
                        BossSelectionItem(
                            bossName = monsName,
                            onClickBossItem = {
                                coroutineScope.launch {
                                    setSelectedMonsterName(monsName)
                                    bottomSheetState.show()
                                    localAnalyticsLoggingEvent(
                                        AnalyticsEvent.FrequentlyUsedBoss(monsName = monsName)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

private fun Context.startOverlayService(
    status: Boolean
) = this.startForegroundService(
    Intent(
        this,
        OverlayService::class.java
    ).apply {
        putExtra("status", status)
    }
)

@Composable
private fun BossSelectionItem(
    bossName: String,
    onClickBossItem: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            text = bossName,
            onClick = { onClickBossItem(bossName) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWatchScreen() =
    PreviewMiscellaneousToolTheme {
        WatchScreen(
            uiState = WatchUiState(
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
                selectedMonsterName = "",
                timerList = emptyList(),
                watchStatus = ButtonStatus.OFF,
                fontSize = 14,
                xPos = 0,
                yPos = 0
            ),
            setWatchStatus = {},
            setFontSize = {},
            setXPos = {},
            setYPos = {},
            setTimerSetting = {},
            onNavigatePopBackStack = {},
            setSelectedMonsterName = {},
            setSelectedMonsterOtaToTrue = {}
        )
    }