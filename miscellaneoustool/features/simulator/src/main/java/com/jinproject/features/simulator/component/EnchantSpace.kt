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
import com.jinproject.features.simulator.EquipmentListPreviewParameters
import com.jinproject.features.simulator.model.Armor
import com.jinproject.features.simulator.model.Empty
import com.jinproject.features.simulator.model.EnchantScroll
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.Item
import com.jinproject.features.simulator.model.Weapon
import com.jinproject.features.simulator.model.findEnchantScroll
import com.jinproject.features.simulator.model.formatter
import com.jinproject.features.simulator.util.enchantEquipmentBy30percent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EnchantSpace(
    size: Dp,
    selectedItem: Equipment,
    setSelectedEquipment: (Equipment) -> Unit,
    storeSelectedItem: () -> Unit,
) {
    val dndTarget = remember(selectedItem) {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val draggedData = event.toAndroidDragEvent().clipData.getItemAt(0).text.toString()
                val decodedData = formatter.decodeFromString<Item>(draggedData)

                val newItem = if (decodedData is EnchantScroll) {
                    when (selectedItem) {
                        is Weapon -> {
                            selectedItem.copy(
                                enchantNumber = enchantEquipmentBy30percent(
                                    now = selectedItem.enchantNumber,
                                    standard = 6,
                                )
                            )
                        }

                        is Armor -> {
                            selectedItem.copy(
                                enchantNumber = enchantEquipmentBy30percent(
                                    now = selectedItem.enchantNumber,
                                    standard = 4,
                                )
                            )
                        }

                        else -> throw IllegalStateException("$selectedItem is not allowed type for Equipment")
                    }
                } else
                    decodedData as Equipment

                setSelectedEquipment(newItem)

                if (decodedData is EnchantScroll)
                    storeSelectedItem()

                return true
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalWeightSpacer(float = 1f)
        if (selectedItem !is Empty) {
            ItemDetail(equipment = selectedItem)
            HorizontalWeightSpacer(float = 1f)
        }
        ItemSpace(
            modifier = Modifier.dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)

                }, target = dndTarget
            ),
            size = size,
            item = selectedItem,
        )
        if (selectedItem !is Empty) {
            HorizontalWeightSpacer(float = 1f)
            Image(
                painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_arrow_left),
                contentDescription = "Enchant Arrow",
                colorFilter = ColorFilter.tint(MiscellaneousToolColor.itemButtonColor.color)
            )
            HorizontalWeightSpacer(float = 1f)

            ItemSpace(
                modifier = Modifier.dragAndDropSource {
                    detectTapGestures(onPress = {
                        startTransfer(
                            DragAndDropTransferData(
                                ClipData.newPlainText(
                                    "data",
                                    formatter.encodeToString<EnchantScroll>(selectedItem.level.findEnchantScroll())
                                )
                            )
                        )
                    })
                },
                size = size,
                item = selectedItem.level.findEnchantScroll(),
            )
        }
        HorizontalWeightSpacer(float = 1f)
    }
}

@Preview(heightDp = 180)
@Composable
private fun PreviewEnchantSpace(
    @PreviewParameter(EquipmentListPreviewParameters::class)
    equipments: ImmutableList<Equipment>,
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
            selectedItem = equipments.first(),
            setSelectedEquipment = {},
            storeSelectedItem = {},
        )
    }
}