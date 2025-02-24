package com.jinproject.features.simulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.model.Stat
import com.jinproject.domain.usecase.simulator.GetAvailableItemInfoUseCase
import com.jinproject.domain.usecase.simulator.GetOwnedItemsUseCase
import com.jinproject.domain.usecase.simulator.UpdateItemOnOwnedItemListUseCase
import com.jinproject.features.simulator.model.Armor
import com.jinproject.features.simulator.model.Empty
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.SimulatorState
import com.jinproject.features.simulator.model.Weapon
import com.jinproject.features.simulator.model.toEquipment
import com.jinproject.features.simulator.model.toItemInfoDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
internal class SimulatorViewModel @Inject constructor(
    getAvailableItemInfoUseCase: GetAvailableItemInfoUseCase,
    getOwnedItemsUseCase: GetOwnedItemsUseCase,
    private val updateItemOnOwnedItemsUseCase: UpdateItemOnOwnedItemListUseCase,
) : ViewModel() {

    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    val simulatorState: StateFlow<SimulatorState> =
        getAvailableItemInfoUseCase().combine(getOwnedItemsUseCase()) { available, owned ->
            SimulatorState(
                ownedItems = owned.map { it.toEquipment() }.toPersistentList(),
                availableItems = available.map {
                    it.copy(uuid = Uuid.random().toString()).toEquipment()
                }.toImmutableList(),
            )
        }.flatMapLatest { simulatorState ->
            flow {
                selectedEquipment.collect { selected ->
                    val idx = simulatorState.ownedItems.indexOfFirst { owned ->
                        owned.uuid.contentEquals(selected.uuid)
                    }
                    val newOwnedItems = if (idx != -1) {
                        val newEquipment = when (val owned = simulatorState.ownedItems[idx]) {
                            is Weapon -> owned.copy(enchantNumber = selected.enchantNumber)
                            is Armor -> owned.copy(enchantNumber = selected.enchantNumber)
                            else -> owned
                        }

                        simulatorState.ownedItems.set(idx, newEquipment)
                    } else
                        simulatorState.ownedItems

                    emit(
                        simulatorState.copy(
                            ownedItems = newOwnedItems
                        )
                    )
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SimulatorState.getInitValues(),
        )

    private val _selectedEquipment: MutableStateFlow<Equipment> = MutableStateFlow(Empty())
    val selectedEquipment: StateFlow<Equipment> get() = _selectedEquipment.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    fun addEquipmentOnOwnedItemList(equipment: Equipment) {
        viewModelScope.launch {
            val itemInfo = equipment.toItemInfoDomainModel()
            val processed = itemInfo.copy(
                uuid = Uuid.random().toString(),
                stat = mutableMapOf<String, Float>().apply {
                    itemInfo.stat.forEach { s ->
                        put(s.key, Stat.entries.firstOrNull { it.name == s.key }?.range?.random()?.toFloat() ?: s.value)
                    }
                }
            )

            updateItemOnOwnedItemsUseCase.add(processed)
        }
    }

    fun removeEquipmentOnOwnedItemList(uuid: String) {
        viewModelScope.launch {
            updateItemOnOwnedItemsUseCase.remove(uuid)
        }
    }

    fun updateSelectedEquipment(equipment: Equipment) {
        viewModelScope.launch {
            _selectedEquipment.update { equipment }
        }
    }

    fun storeSelectedItem() {
        viewModelScope.launch {
            if (selectedEquipment.value !is Empty)
                updateItemOnOwnedItemsUseCase.replace(selectedEquipment.value.toItemInfoDomainModel())
        }
    }

}