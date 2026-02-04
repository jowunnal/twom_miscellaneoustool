package com.jinproject.features.simulator.component

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.simulator.SimulatorStatePreviewParameters
import com.jinproject.features.simulator.model.Empty
import com.jinproject.features.simulator.model.EnchantScroll
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.Item
import com.jinproject.features.simulator.model.SimulatorState
import com.jinproject.features.simulator.model.findEnchantScroll
import com.jinproject.features.simulator.model.formatter
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EnchantSpace(
    size: Dp,
    simulatorState: SimulatorState,
    updateSelectedItem: (Equipment) -> Unit,
    enchantEquipment: (Equipment, EnchantScroll) -> Unit,
) {
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    val dndTarget = remember(simulatorState) {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val draggedData = event.toAndroidDragEvent().clipData.getItemAt(0).text.toString()

                when (val decodedData = formatter.decodeFromString<Item>(draggedData)) {
                    is EnchantScroll -> {
                        val item =
                            simulatorState.ownedItems.find { it.uuid == simulatorState.selectedItem.uuid }
                                ?: simulatorState.selectedItem
                        enchantEquipment(
                            item,
                            decodedData
                        )

                        localAnalyticsLoggingEvent(
                            AnalyticsEvent.SimulatorEnchant(
                                itemName = simulatorState.selectedItem.name,
                                result = true
                            )
                        )
                    }

                    is Equipment -> updateSelectedItem(decodedData)

                    else -> return false
                }

                return true
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalWeightSpacer(float = 1f)
        if (simulatorState.selectedItem !is Empty) {
            ItemDetail(equipment = simulatorState.selectedItem)
            HorizontalWeightSpacer(float = 1f)
        }
        ItemSpace(
            modifier = Modifier.dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)

                }, target = dndTarget
            ),
            size = size,
            item = simulatorState.selectedItem,
        )
        if (simulatorState.selectedItem !is Empty) {
            HorizontalWeightSpacer(float = 1f)
            Image(
                painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_arrow_left),
                contentDescription = "Enchant Arrow",
                colorFilter = ColorFilter.tint(MiscellaneousToolColor.itemButtonColor.color)
            )
            HorizontalWeightSpacer(float = 1f)

            ItemSpace(
                modifier = Modifier.dragAndDropSource { offset ->
                    DragAndDropTransferData(
                        ClipData.newPlainText(
                            "data",
                            formatter.encodeToString<EnchantScroll>(simulatorState.selectedItem.level.findEnchantScroll())
                        )
                    )
                },
                size = size,
                item = simulatorState.selectedItem.level.findEnchantScroll(),
            )
        }
        HorizontalWeightSpacer(float = 1f)
    }
}

@Preview(heightDp = 180)
@Composable
private fun PreviewEnchantSpace(
    @PreviewParameter(SimulatorStatePreviewParameters::class)
    simulatorState: SimulatorState,
) = MiscellaneousToolTheme {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        EnchantSpace(
            size = 64.dp,
            simulatorState = simulatorState,
            updateSelectedItem = {},
            enchantEquipment = { _, _ -> },
        )
    }
}