package com.jinproject.features.simulator

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.paddingvalues.addStatusBarPadding
import com.jinproject.features.simulator.component.AvailableEquipmentSheet
import com.jinproject.features.simulator.component.EnchantSpace
import com.jinproject.features.simulator.component.EquipmentDeleteBar
import com.jinproject.features.simulator.component.ItemList
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.SimulatorState

@Composable
internal fun SimulatorScreen(
    simulatorViewModel: SimulatorViewModel = hiltViewModel(),
) {
    val simulatorState by simulatorViewModel.simulatorState.collectAsStateWithLifecycle()
    val selectedEquipment by simulatorViewModel.selectedEquipment.collectAsStateWithLifecycle()

    SimulatorScreen(
        simulatorState = simulatorState,
        selectedEquipment = selectedEquipment,
        updateSelectedEquipment = simulatorViewModel::updateSelectedEquipment,
        addEquipmentOnOwnedItemList = simulatorViewModel::addEquipmentOnOwnedItemList,
        removeEquipmentOnOwnedItemListByUUID = simulatorViewModel::removeEquipmentOnOwnedItemList,
        storeSelectedItem = simulatorViewModel::storeSelectedItem,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimulatorScreen(
    configuration: Configuration = LocalConfiguration.current,
    simulatorState: SimulatorState,
    selectedEquipment: Equipment,
    updateSelectedEquipment: (Equipment) -> Unit,
    addEquipmentOnOwnedItemList: (Equipment) -> Unit,
    removeEquipmentOnOwnedItemListByUUID: (String) -> Unit,
    storeSelectedItem: () -> Unit,
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

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 20.dp)
            .addStatusBarPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EquipmentDeleteBar(
            isEquipmentDragging = isEquipmentDragging,
            setIsEquipmentDragging = { bool -> isEquipmentDragging = bool },
            removeEquipmentOnOwnedItemList = removeEquipmentOnOwnedItemListByUUID,
        )
        VerticalSpacer(height = 100.dp)
        EnchantSpace(
            size = itemWidthDp,
            selectedItem = selectedEquipment,
            setSelectedEquipment = { equipment ->
                updateSelectedEquipment(equipment)
            },
            storeSelectedItem = storeSelectedItem,
            removeEquipmentOnOwnedItemListByUUID = removeEquipmentOnOwnedItemListByUUID,
        )
        VerticalSpacer(height = 200.dp)
        ItemList(
            itemCount = itemCount,
            itemPadding = PaddingValues(itemPadding),
            itemSizeDp = itemWidthDp,
            simulatorState = simulatorState,
            selectedEquipment = selectedEquipment,
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
        selectedEquipment = simulatorState.ownedItems.first(),
        updateSelectedEquipment = {},
        addEquipmentOnOwnedItemList = {},
        removeEquipmentOnOwnedItemListByUUID = {},
        storeSelectedItem = {},
    )
}