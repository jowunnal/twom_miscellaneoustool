package com.jinproject.features.simulator.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.features.simulator.EquipmentListPreviewParameters
import com.jinproject.features.simulator.model.Equipment
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AvailableEquipmentSheet(
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    isBottomSheetOpened: Boolean,
    updateBottomSheetOpened: (Boolean) -> Unit,
    itemCount: Int,
    itemPadding: PaddingValues,
    itemSizeDp: Dp,
    availableItems: ImmutableList<Equipment>,
    addOnItemSpace: (Equipment) -> Unit,
) {
    if (isBottomSheetOpened) {
        ModalBottomSheet(
            onDismissRequest = { updateBottomSheetOpened(false) },
            sheetState = bottomSheetState,
            dragHandle = {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_handle_bar),
                        contentDescription = "HandleBar",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                updateBottomSheetOpened(false)
                            },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_x),
                                contentDescription = "ExitIcon",
                                tint = MaterialTheme.colorScheme.scrim
                            )
                        }
                    }

                }
            }
        ) {
            AvailableEquipmentSheetContent(
                itemCount = itemCount,
                itemPadding = itemPadding,
                itemSizeDp = itemSizeDp,
                availableItems = availableItems,
                addOnItemSpace = addOnItemSpace,
            )
        }
    }
}

@Composable
private fun AvailableEquipmentSheetContent(
    itemCount: Int,
    itemPadding: PaddingValues,
    itemSizeDp: Dp,
    availableItems: ImmutableList<Equipment>,
    addOnItemSpace: (Equipment) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(itemCount),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MiscellaneousToolColor.itemListContentColor.color,
                    RoundedCornerShape(10.dp)
                )
                .padding(itemPadding),
        ) {
            items(availableItems) { item ->
                ItemSpace(
                    size = itemSizeDp,
                    item = item,
                    modifier = Modifier.clickableAvoidingDuplication {
                        addOnItemSpace(item)
                    }
                )
            }
        }
    }
}

@Preview(heightDp = 640)
@Composable
private fun PreviewAvailableEquipmentSheetContent(
    @PreviewParameter(EquipmentListPreviewParameters::class)
    equipments: ImmutableList<Equipment>,
    configuration: Configuration = LocalConfiguration.current,
) = PreviewMiscellaneousToolTheme {
    val viewWidthDp = configuration.screenWidthDp.dp
    val itemCount = 6
    val itemPadding = 2.dp
    val itemWidthDp = (viewWidthDp - (itemPadding * 2 * itemCount)) / itemCount

    AvailableEquipmentSheetContent(
        itemCount = 6,
        itemPadding = PaddingValues(6.dp),
        itemSizeDp = itemWidthDp,
        availableItems = equipments,
        addOnItemSpace = {},
    )
}