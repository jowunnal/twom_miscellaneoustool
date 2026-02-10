package com.jinproject.features.droplist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.jinproject.design_compose.component.CoilBasicImage
import com.jinproject.design_compose.component.HorizontalDividerItem
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.utils.toAssetImageUri
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
                            monster = monster,
                        )
                    }
                    HorizontalSpacer(width = 30.dp)
                    FlowRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        itemVerticalAlignment = Alignment.CenterVertically,
                    ) {
                        monster.items.forEachIndexed { idx, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (item.imageName.isNotBlank())
                                    CoilBasicImage(
                                        data = toAssetImageUri(
                                            prefix = item.imagePrefix,
                                            imgName = item.imageName
                                        ),
                                        modifier = Modifier,
                                    )
                                DescriptionSmallText(text = "${item.name}${if (idx != monster.items.lastIndex) "," else ""}")
                            }
                        }
                    }
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
    val entry = dropListUiState.monstersGroupedByMap.entries.first()
    DropListDetail(
        mapName = entry.key.name,
        monsterListState = entry.value,
        onNavigateBack = {},
    )
}