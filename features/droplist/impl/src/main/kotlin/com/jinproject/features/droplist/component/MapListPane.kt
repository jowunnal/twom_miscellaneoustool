package com.jinproject.features.droplist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.droplist.DropListUiState
import com.jinproject.features.droplist.DropListUiStatePreviewParameter
import com.jinproject.features.droplist.state.MapState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MapListPane(
    mapListState: ImmutableList<MapState>,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onClickItem: (MapState) -> Unit,
) {
    val itemWidth = 120.dp

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(itemWidth),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(mapListState, key = { mapState -> mapState.name }) { mapState ->
            DropListMonster(
                mapState = mapState,
                itemWidth = itemWidth,
                modifier = Modifier.animateItem(),
                onClickItem = onClickItem,
            )
        }
    }
}

@Composable
fun DropListMonster(
    mapState: MapState,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    onClickItem: (MapState) -> Unit,
) {
    Column(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .clickable {
                onClickItem(mapState)
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        MonsterField(
            modifier = Modifier
                .height(65.dp)
                .width(itemWidth),
            imgName = mapState.imgName,
            content = {
                DescriptionSmallText(text = mapState.name)
            }
        )
    }
}

@Composable
@Preview
private fun MapListPanePreview(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState,
) = MiscellaneousToolTheme {
    MapListPane(
        mapListState = dropListUiState.monstersGroupedByMap.keys.toImmutableList(),
        onClickItem = {},
    )
}