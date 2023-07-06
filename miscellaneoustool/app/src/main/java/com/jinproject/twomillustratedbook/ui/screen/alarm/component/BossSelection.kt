package com.jinproject.twomillustratedbook.ui.screen.alarm.component

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.domain.model.MonsterType
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DialogState
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DropDownMenuCustom
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.Typography
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview

@Composable
fun BossSelection(
    bossNameList: List<String>,
    recentlySelectedBossClassified: String,
    recentlySelectedBossName: String,
    frequentlyUsedBossList: List<String>,
    onClickBossItem: (String) -> Unit,
    addBossToFrequentlyUsedList: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    setRecentlySelectedBossClassifiedChanged:(MonsterType) -> Unit,
    setRecentlySelectedBossNameChanged:(String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    Column() {
        BossSelectionHeader(
            bossNameList = bossNameList,
            recentlySelectedBossClassified = recentlySelectedBossClassified,
            recentlySelectedBossName = recentlySelectedBossName,
            addBossToFrequentlyUsedList = addBossToFrequentlyUsedList,
            setRecentlySelectedBossClassifiedChanged = setRecentlySelectedBossClassifiedChanged,
            setRecentlySelectedBossNameChanged = setRecentlySelectedBossNameChanged
        )

        VerticalSpacer(height = 16.dp)
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.scrim)
        VerticalSpacer(height = 16.dp)
        Text(
            text = stringResource(id = R.string.watch_title_recently_bosslist),
            style = Typography.headlineSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
        )
        VerticalSpacer(height = 16.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(150.dp)
        ) {
            itemsIndexed(frequentlyUsedBossList) { index, item ->
                BossSelectionItem(
                    bossName = item,
                    onClickBossItem = onClickBossItem,
                    removeBossFromFrequentlyUsedList = removeBossFromFrequentlyUsedList,
                    onOpenDialog = onOpenDialog,
                    onCloseDialog = onCloseDialog
                )
            }
        }
    }
}

@Composable
private fun BossSelectionHeader(
    bossNameList: List<String>,
    context: Context = LocalContext.current,
    recentlySelectedBossClassified: String,
    recentlySelectedBossName: String,
    addBossToFrequentlyUsedList: (String) -> Unit,
    setRecentlySelectedBossClassifiedChanged:(MonsterType) -> Unit,
    setRecentlySelectedBossNameChanged:(String) -> Unit,
) {
    Column() {
        Row() {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                DropDownMenuCustom(
                    label = stringResource(id = R.string.alarm_classify_boss),
                    text = recentlySelectedBossClassified,
                    items = MonsterType.values().toMutableList()
                        .apply { remove(MonsterType.NORMAL) }
                        .map { monsterType ->
                            context.doOnLocaleLanguage(
                                onKo = monsterType.displayName,
                                onElse = monsterType.storedName
                            )
                        }
                        .toList(),
                    setTextChanged = { item ->
                        setRecentlySelectedBossClassifiedChanged(
                            context.doOnLocaleLanguage(
                                onKo = MonsterType.findByDisplayName(item),
                                onElse = MonsterType.findByStoredName(item)
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                DropDownMenuCustom(
                    label = stringResource(id = R.string.alarm_selectboss),
                    text = recentlySelectedBossName,
                    items = bossNameList,
                    setTextChanged = { item -> setRecentlySelectedBossNameChanged(item) }
                )
            }
        }
        VerticalSpacer(height = 20.dp)
        DefaultButton(
            content = stringResource(id = R.string.addition_do),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { addBossToFrequentlyUsedList(recentlySelectedBossName) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BossSelectionItem(
    bossName: String,
    context: Context = LocalContext.current,
    onClickBossItem: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultButton(
            content = if (bossName.length > 4) bossName.substring(0..3) + "\n" + bossName.substring(4) else bossName,
            modifier = Modifier.combinedClickable(
                onClick = { onClickBossItem(bossName) },
                onLongClick = {
                    onOpenDialog(
                        DialogState(
                            header = "$bossName ${context.getString(R.string.delete_something)}",
                            positiveMessage = context.getString(R.string.yes),
                            negativeMessage = context.getString(R.string.no),
                            onPositiveCallback = {
                                removeBossFromFrequentlyUsedList(bossName)
                                onCloseDialog()
                            },
                            onNegativeCallback = { onCloseDialog() }
                        )
                    )
                }
            ),
            style = Typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBossSelectionItem() =
    TwomIllustratedBookPreview {
        BossSelectionItem(
            bossName = "보스이름",
            onClickBossItem = {},
            removeBossFromFrequentlyUsedList = {},
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }


@Preview(showBackground = true)
@Composable
private fun PreviewBossSelection() =
    TwomIllustratedBookPreview {
        BossSelection(
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
            recentlySelectedBossClassified = "네임드",
            recentlySelectedBossName = "보스1",
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
            onClickBossItem = {},
            addBossToFrequentlyUsedList = {},
            removeBossFromFrequentlyUsedList = {},
            setRecentlySelectedBossClassifiedChanged = {},
            setRecentlySelectedBossNameChanged = {},
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }
