package com.jinproject.features.droplist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalDividerItem
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.droplist.DropListUiState
import com.jinproject.features.droplist.DropListUiStatePreviewParameter
import com.jinproject.features.droplist.state.MonsterState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DropListDetail(
    mapName: String,
    monsterListState: ImmutableList<MonsterState>,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = onNavigateBack,
                title = mapName,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = MiscellanousToolPaddingValues.fromPaddingValues(paddingValues) + MiscellanousToolPaddingValues(
                start = 6.dp,
                end = 6.dp,
            )
        ) {
            itemsIndexed(monsterListState, key = { _, monster -> monster.name }) { idx, monster ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Monster(
                            modifier = Modifier.size(80.dp),
                            imgName = monster.imgName,
                            header = monster.name,
                            tail = monster.level.toString(),
                        )
                    }
                    HorizontalSpacer(width = 30.dp)
                    DescriptionSmallText(text = monster.itemsToSingleLine())
                }
                HorizontalDividerItem(
                    idx = idx,
                    lastIdx = monsterListState.lastIndex,
                )
            }
        }
    }
}

@Composable
@Preview
private fun DropListDetailPreview(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState,
) = MiscellaneousToolTheme {
    DropListDetail(
        monsterListState = dropListUiState.monsters,
        onNavigateBack = {},
        mapName = dropListUiState.maps.first().name,
    )
}