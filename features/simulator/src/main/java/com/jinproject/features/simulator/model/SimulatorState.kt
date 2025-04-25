package com.jinproject.features.simulator.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
internal data class SimulatorState(
    val ownedItems: PersistentList<Equipment>,
    val availableItems: ImmutableList<Equipment>,
    val selectedItem: Equipment,
) {
    companion object {
        fun getInitValues() = SimulatorState(
            ownedItems = persistentListOf(),
            availableItems = persistentListOf(),
            selectedItem = Empty(),
        )
    }
}