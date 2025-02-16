package com.jinproject.features.simulator.component

import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.image.DefaultPainterImage
import com.jinproject.design_compose.component.text.DescriptionMediumText
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.formatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EquipmentDeleteBar(
    isEquipmentDragging: Boolean,
    setIsEquipmentDragging: (Boolean) -> Unit,
    removeEquipmentOnOwnedItemList: (String) -> Unit,
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    var deleteAreaColor by remember {
        mutableStateOf(surfaceColor)
    }

    val dndTarget = remember() {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val draggedData = event.toAndroidDragEvent().clipData.getItemAt(0).text.toString()
                val decodedData = formatter.decodeFromString<Equipment>(draggedData)

                removeEquipmentOnOwnedItemList(decodedData.uuid)

                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                deleteAreaColor = MiscellaneousToolColor.red.color
            }

            override fun onExited(event: DragAndDropEvent) {
                super.onExited(event)
                deleteAreaColor = surfaceColor
            }

            override fun onEnded(event: DragAndDropEvent) {
                super.onEnded(event)
                deleteAreaColor = surfaceColor
                setIsEquipmentDragging(false)
            }

        }
    }

    AnimatedVisibility(visible = isEquipmentDragging) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .dragAndDropTarget(shouldStartDragAndDrop = { event ->
                    event
                        .mimeTypes()
                        .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)

                }, target = dndTarget)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .shadow(5.dp, shape = RoundedCornerShape(100.dp))
                .background(deleteAreaColor, shape = RoundedCornerShape(100.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            DefaultPainterImage(
                resId = com.jinproject.design_ui.R.drawable.ic_delete,
                contentDescription = "Delete OwnedItem Icon",
            )
            HorizontalSpacer(width = 8.dp)
            DescriptionMediumText(text = "삭제")
        }
    }
}

@Preview
@Composable
private fun PreviewEquipmentDeleteBar() = MiscellaneousToolTheme {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        EquipmentDeleteBar(
            isEquipmentDragging = true,
            setIsEquipmentDragging = {},
            removeEquipmentOnOwnedItemList = {},
        )
    }
}