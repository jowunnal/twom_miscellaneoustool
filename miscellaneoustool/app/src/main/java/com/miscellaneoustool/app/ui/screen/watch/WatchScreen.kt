package com.miscellaneoustool.app.ui.screen.watch

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.miscellaneoustool.app.ui.screen.compose.component.DefaultAppBar
import com.miscellaneoustool.app.ui.screen.compose.component.DefaultButton
import com.miscellaneoustool.app.ui.screen.compose.component.DefaultLayout
import com.miscellaneoustool.app.ui.screen.compose.component.VerticalSpacer
import com.miscellaneoustool.app.ui.screen.compose.theme.deepGray
import com.miscellaneoustool.app.ui.screen.watch.component.TimeStatusSetting
import com.miscellaneoustool.app.ui.screen.watch.component.TimerBottomSheetContent
import com.miscellaneoustool.app.ui.screen.watch.component.TimerFontSizeSetting
import com.miscellaneoustool.app.ui.screen.watch.item.ButtonStatus
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.tu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WatchScreen(
    uiState: WatchUiState,
    activityContext: Context,
    setWatchStatus: (ButtonStatus) -> Unit,
    setFontSize: (Int) -> Unit,
    setSelectedMonsterName: (String) -> Unit,
    checkAuthorityDrawOverlays: (Context, (Intent) -> Unit) -> Boolean,
    onNavigatePopBackStack: () -> Unit,
    setSelectedMonsterOtaToTrue: (Int) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

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
                    timerList = uiState.timerList,
                    watchStatus = uiState.watchStatus,
                    fontSize = uiState.fontSize,
                    activityContext = activityContext,
                    setWatchStatus = setWatchStatus,
                    checkAuthorityDrawOverlays = checkAuthorityDrawOverlays
                )
                TimerFontSizeSetting(
                    fontSize = uiState.fontSize,
                    setFontSize = setFontSize
                )

                VerticalSpacer(height = 16.dp)
                Text(
                    text = "자주 사용하는 보스 목록",
                    fontSize = 18.tu,
                    fontWeight = FontWeight.ExtraBold,
                    color = deepGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
                VerticalSpacer(height = 16.dp)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(150.dp)
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
            fontSize = 16
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
                fontSize = 14
            ),
            setWatchStatus = {},
            setFontSize = {},
            activityContext = object : FragmentActivity() {},
            checkAuthorityDrawOverlays = { _, _ -> false },
            onNavigatePopBackStack = {},
            setSelectedMonsterName = {},
            setSelectedMonsterOtaToTrue = {}
        )
    }