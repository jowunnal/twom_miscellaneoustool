package com.jinproject.features.simulator

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.features.simulator.component.AvailableEquipmentSheet
import com.jinproject.features.simulator.component.EnchantSpace
import com.jinproject.features.simulator.component.EquipmentDeleteBar
import com.jinproject.features.simulator.component.ItemList
import com.jinproject.features.simulator.model.EnchantScroll
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.SimulatorState

@Composable
internal fun SimulatorScreen(
    simulatorViewModel: SimulatorViewModel = hiltViewModel(),
) {
    val simulatorState by simulatorViewModel.simulatorState.collectAsStateWithLifecycle()

    SimulatorScreen(
        simulatorState = simulatorState,
        addEquipmentOnOwnedItemList = simulatorViewModel::addEquipmentOnOwnedItemList,
        removeEquipmentOnOwnedItemListByUUID = simulatorViewModel::removeItemOnOwnedItemList,
        enchantEquipment = simulatorViewModel::enchantEquipment,
        updateSelectedItem = simulatorViewModel::updateSelectedItem,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimulatorScreen(
    configuration: Configuration = LocalConfiguration.current,
    simulatorState: SimulatorState,
    addEquipmentOnOwnedItemList: (Equipment) -> Unit,
    removeEquipmentOnOwnedItemListByUUID: (String) -> Unit,
    enchantEquipment: (Equipment, EnchantScroll) -> Unit,
    updateSelectedItem: (Equipment) -> Unit,
) {
    val viewWidthDp = configuration.screenWidthDp.dp
    val itemCount = 6
    val itemPadding = 2.dp
    val itemWidthDp = (viewWidthDp - (itemPadding * 2 * itemCount)) / itemCount

    var isBottomSheetOpened by remember {
        mutableStateOf(false)
    }

    var isEquipmentDragging by remember {
        mutableStateOf(false)
    }
    val localAnalyticsLoggingEvent = LocalAnalyticsLoggingEvent.current

    DefaultLayout(
        contentPaddingValues = MiscellanousToolPaddingValues(top = 20.dp),
    ) {
        EquipmentDeleteBar(
            isEquipmentDragging = isEquipmentDragging,
            setIsEquipmentDragging = { bool -> isEquipmentDragging = bool },
            removeEquipmentOnOwnedItemList = removeEquipmentOnOwnedItemListByUUID,
        )
        VerticalSpacer(height = 100.dp)
        EnchantSpace(
            size = itemWidthDp,
            enchantEquipment = enchantEquipment,
            simulatorState = simulatorState,
            updateSelectedItem = updateSelectedItem,
        )
        VerticalSpacer(height = 200.dp)
        ItemList(
            itemCount = itemCount,
            itemPadding = PaddingValues(itemPadding),
            itemSizeDp = itemWidthDp,
            simulatorState = simulatorState,
            setIsEquipmentDragging = { bool -> isEquipmentDragging = bool },
            showBottomSheet = { isBottomSheetOpened = true },
        )
        VerticalSpacer(height = 100.dp)
    }

    AvailableEquipmentSheet(
        isBottomSheetOpened = isBottomSheetOpened,
        updateBottomSheetOpened = { bool -> isBottomSheetOpened = bool },
        itemCount = itemCount,
        itemPadding = PaddingValues(itemPadding),
        itemSizeDp = itemWidthDp,
        availableItems = simulatorState.availableItems,
        addOnItemSpace = { equipment ->
            addEquipmentOnOwnedItemList(equipment)
            localAnalyticsLoggingEvent(
                com.jinproject.features.core.AnalyticsEvent.SimulatorAddItem(
                    itemName = equipment.name
                )
            )
        }
    )
}

@Preview
@Composable
private fun PreviewSimulatorScreen(
    @PreviewParameter(SimulatorStatePreviewParameters::class)
    simulatorState: SimulatorState,
) = PreviewMiscellaneousToolTheme {

    SimulatorScreen(
        simulatorState = simulatorState,
        addEquipmentOnOwnedItemList = {},
        removeEquipmentOnOwnedItemListByUUID = {},
        enchantEquipment = { _, _ -> },
        updateSelectedItem = {},
    )
}