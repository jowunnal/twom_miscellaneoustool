package com.jinproject.features.droplist.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.droplist.DropListUiState
import com.jinproject.features.droplist.DropListUiStatePreviewParameter
import com.jinproject.features.droplist.state.MapState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MapListPane(
    mapListState: ImmutableList<MapState>,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    configuration: Configuration = LocalConfiguration.current,
    onClickItem: (MapState) -> Unit,
) {

    val itemCount = when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED -> 6
        else -> 3
    }
    val itemSpaceWidth = 16.dp
    val itemPadding = 12.dp
    val itemWidth =
        (configuration.screenWidthDp.dp - itemSpaceWidth * (itemCount - 1) - itemPadding * 2) / itemCount

    LazyVerticalGrid(
        columns = GridCells.Adaptive(itemWidth),
        verticalArrangement = Arrangement.spacedBy(itemSpaceWidth),
        horizontalArrangement = Arrangement.spacedBy(itemSpaceWidth),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = itemPadding)
    ) {
        items(mapListState, key = { mapState -> mapState.name }) { mapState ->
            DropListMonster(
                mapState = mapState,
                itemWidth = itemWidth,
                onClickItem = onClickItem,
            )
        }
    }
}

@Composable
fun DropListMonster(
    mapState: MapState,
    itemWidth: Dp,
    onClickItem: (MapState) -> Unit,
) {
    Column(
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .clickable {
                onClickItem(mapState)
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Monster(
            modifier = Modifier
                .height(65.dp)
                .width(itemWidth),
            imgName = mapState.imgName,
            header = mapState.name,
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
        mapListState = dropListUiState.maps,
        onClickItem = {},
    )
}