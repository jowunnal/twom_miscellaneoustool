package com.jinproject.features.simulator.component

import android.content.ClipData
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.simulator.SimulatorStatePreviewParameters
import com.jinproject.features.simulator.model.Empty
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.SimulatorState
import com.jinproject.features.simulator.model.formatter
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ItemList(
    modifier: Modifier = Modifier,
    itemCount: Int,
    itemPadding: PaddingValues,
    itemSizeDp: Dp,
    simulatorState: SimulatorState,
    selectedEquipment: Equipment,
    setIsEquipmentDragging: (Boolean) -> Unit,
    showBottomSheet: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(itemCount),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MiscellaneousToolColor.itemListContentColor.color,
                RoundedCornerShape(10.dp)
            )
            .padding(4.dp)
            .then(modifier)
    ) {
        items(simulatorState.ownedItems, key = { item -> item.uuid }) { item ->
            Box(Modifier.padding(itemPadding)) {
                ItemSpace(
                    size = itemSizeDp,
                    item = item,
                    modifier = Modifier
                        .then(
                            if (!selectedEquipment.uuid.contentEquals(item.uuid))
                                Modifier.dragAndDropSource {
                                    detectTapGestures(onPress = {
                                        startTransfer(
                                            DragAndDropTransferData(
                                                ClipData.newPlainText(
                                                    "data", formatter.encodeToString(item)
                                                )
                                            )
                                        )
                                        setIsEquipmentDragging(true)
                                    })
                                }
                            else Modifier
                        )
                        .alpha(if (selectedEquipment.uuid.contentEquals(item.uuid)) 0.3f else 1f)
                )
            }
        }

        item {
            ItemSpace(
                size = itemSizeDp,
                item = Empty().copy(imgName = "icon_plus"),
                modifier = Modifier.clickableAvoidingDuplication {
                    showBottomSheet()
                },
            )
        }
    }
}

@Preview(widthDp = 320, heightDp = 320)
@Composable
private fun PreviewItemList(
    @PreviewParameter(SimulatorStatePreviewParameters::class)
    simulatorState: SimulatorState,
    configuration: Configuration = LocalConfiguration.current,
) = MiscellaneousToolTheme {
    val viewWidthDp = configuration.screenWidthDp.dp
    val itemCount = 6
    val itemPadding = 2.dp
    val itemWidthDp = (viewWidthDp - (itemPadding * 2 * itemCount)) / itemCount

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ItemList(
            itemCount = itemCount,
            itemPadding = PaddingValues(itemPadding),
            itemSizeDp = itemWidthDp,
            simulatorState = simulatorState,
            selectedEquipment = Empty(),
            setIsEquipmentDragging = {},
            showBottomSheet = {},
        )
    }
}

@Preview(widthDp = 320, heightDp = 320)
@Composable
private fun PreviewItemListSelected(
    @PreviewParameter(SimulatorStatePreviewParameters::class)
    simulatorState: SimulatorState,
    configuration: Configuration = LocalConfiguration.current,
) = MiscellaneousToolTheme {
    val viewWidthDp = configuration.screenWidthDp.dp
    val itemCount = 6
    val itemPadding = 2.dp
    val itemWidthDp = (viewWidthDp - (itemPadding * 2 * itemCount)) / itemCount

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ItemList(
            itemCount = itemCount,
            itemPadding = PaddingValues(itemPadding),
            itemSizeDp = itemWidthDp,
            simulatorState = simulatorState,
            selectedEquipment = Empty(),
            setIsEquipmentDragging = {},
            showBottomSheet = {},
        )
    }
}
