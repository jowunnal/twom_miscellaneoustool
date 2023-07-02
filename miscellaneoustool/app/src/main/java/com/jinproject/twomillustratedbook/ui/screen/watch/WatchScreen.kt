package com.jinproject.twomillustratedbook.ui.screen.watch

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultAppBar
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultLayout
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.Typography
import com.jinproject.twomillustratedbook.ui.screen.watch.component.OverlaySetting
import com.jinproject.twomillustratedbook.ui.screen.watch.component.TimeStatusSetting
import com.jinproject.twomillustratedbook.ui.screen.watch.component.TimerBottomSheetContent
import com.jinproject.twomillustratedbook.ui.screen.watch.item.ButtonStatus
import com.jinproject.twomillustratedbook.ui.service.OverlayService
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WatchScreen(
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    watchViewModel: WatchViewModel = hiltViewModel(),
    onNavigatePopBackStack: () -> Unit
) {
    changeVisibilityBottomNavigationBar(false)

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WatchScreen(
    uiState: WatchUiState,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
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

    DefaultLayout(
        topBar = {
            DefaultAppBar(
                title = "현재 시간 보기 설정",
                onBackClick = onNavigatePopBackStack
            )
        },
        scaffoldState = scaffoldState
    ) {
        ModalBottomSheetLayout(
            sheetContent = {
                TimerBottomSheetContent(
                    selectedMonsterName = uiState.selectedMonsterName,
                    onCloseBottomSheet = {
                        coroutineScope.launch {
                            bottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    setSelectedMonsterOtaToTrue = setSelectedMonsterOtaToTrue
                )
            },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TimeStatusSetting(
                    watchStatus = uiState.watchStatus,
                    setWatchStatus = setWatchStatus,
                    startOverlayService = { status ->
                        context.startOverlayService(
                            status = status,
                            fontSize = uiState.fontSize,
                            timerList = uiState.timerList,
                            xPos = uiState.xPos,
                            yPos = uiState.yPos
                        )
                    }
                )

                VerticalSpacer(height = 20.dp)

                OverlaySetting(
                    fontSize = uiState.fontSize,
                    xPos = uiState.xPos,
                    yPos = uiState.yPos,
                    setFontSize = setFontSize,
                    setXPos = setXPos,
                    setYPos = setYPos
                )

                DefaultButton(
                    content = "설정하기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            setTimerSetting()
                            if(uiState.watchStatus == ButtonStatus.ON)
                                context.startOverlayService(
                                    status = false,
                                    fontSize = uiState.fontSize,
                                    timerList = uiState.timerList,
                                    xPos = uiState.xPos,
                                    yPos = uiState.yPos
                                )
                        }
                )

                VerticalSpacer(height = 26.dp)
                Text(
                    text = "자주 사용하는 보스 목록",
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
                VerticalSpacer(height = 16.dp)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3)
                ) {
                    itemsIndexed(uiState.frequentlyUsedBossList) { index, item ->
                        BossSelectionItem(
                            bossName = item,
                            onClickBossItem = {
                                coroutineScope.launch {
                                    setSelectedMonsterName(item)
                                    bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
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
    status: Boolean,
    fontSize: Int,
    timerList: List<TimerState>,
    xPos: Int,
    yPos: Int
) = this.startForegroundService(
    Intent(
        this,
        OverlayService::class.java
    ).apply {
        putExtra("status", status)
        putExtra("fontSize", fontSize)
        if (timerList.isNotEmpty())
            putParcelableArrayListExtra(
                "timerList",
                timerList as ArrayList
            )
        putExtra("xPos", xPos)
        putExtra("yPos", yPos)
    }
)


@OptIn(ExperimentalFoundationApi::class)
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
        DefaultButton(
            content = if (bossName.length > 4) bossName.substring(0..3) + "\n" + bossName.substring(
                4
            ) else bossName,
            modifier = Modifier.combinedClickable(
                onClick = { onClickBossItem(bossName) }
            ),
            style = Typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWatchScreen() =
    TwomIllustratedBookPreview {
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