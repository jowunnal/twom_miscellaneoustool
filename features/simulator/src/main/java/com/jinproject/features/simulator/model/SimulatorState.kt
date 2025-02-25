package com.jinproject.features.simulator.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal data class SimulatorState(
    val ownedItems: PersistentList<Equipment>,
    val availableItems: ImmutableList<Equipment>,
) {
    companion object {
        fun getInitValues() = SimulatorState(
            ownedItems = persistentListOf(),
            availableItems = persistentListOf()
        )
    }
}