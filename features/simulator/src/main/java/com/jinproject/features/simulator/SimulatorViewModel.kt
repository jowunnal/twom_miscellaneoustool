package com.jinproject.features.simulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.usecase.simulator.EnchantEquipmentUseCase
import com.jinproject.domain.usecase.simulator.GetEquipmentsUseCase
import com.jinproject.domain.usecase.simulator.OwnedItemsUseCase
import com.jinproject.features.core.utils.RestartableStateFlow
import com.jinproject.features.core.utils.restartableStateIn
import com.jinproject.features.simulator.model.Empty
import com.jinproject.features.simulator.model.EnchantScroll
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.SimulatorState
import com.jinproject.features.simulator.model.fromDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
internal class SimulatorViewModel @Inject constructor(
    getEquipmentsUseCase: GetEquipmentsUseCase,
    private val ownedItemsUseCase: OwnedItemsUseCase,
    private val enchantEquipmentUseCase: EnchantEquipmentUseCase,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
    val simulatorState: RestartableStateFlow<SimulatorState> =
        getEquipmentsUseCase().combine(ownedItemsUseCase.getItems()) { available, owned ->
            val ownedItems = owned.map { it.fromDomainModel() }.toPersistentList()
            val newSelectedItem = ownedItems.find { it.uuid == selectedItem.value.uuid } ?: Empty()
            SimulatorState(
                availableItems = available.map {
                    it.fromDomainModel(
                        uuid = Uuid.random().toString()
                    )
                }.toImmutableList(),
                ownedItems = ownedItems,
                selectedItem = if (selectedItem.value !is Empty) newSelectedItem else selectedItem.value,
            ).also {
                updateSelectedItem(newSelectedItem)
            }
        }.flatMapLatest { state ->
            selectedItem.map {
                if (it !is Empty)
                    state.copy(selectedItem = it)
                else
                    state
            }
        }.restartableStateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SimulatorState.getInitValues(),
        )

    private val selectedItem: MutableStateFlow<Equipment> = MutableStateFlow(Empty())

    fun updateSelectedItem(equipment: Equipment) = selectedItem.update { equipment }

    @OptIn(ExperimentalUuidApi::class)
    fun addEquipmentOnOwnedItemList(equipment: Equipment) {
        viewModelScope.launch {
            ownedItemsUseCase.add(equipment.toDomainModel(Uuid.random().toString()))
        }
    }

    fun removeItemOnOwnedItemList(uuid: String) {
        viewModelScope.launch {
            ownedItemsUseCase.remove(uuid)
        }
    }

    private fun replaceOwnedItem(equipment: Equipment) {
        viewModelScope.launch {
            ownedItemsUseCase.replace(equipment.toDomainModel())
            simulatorState.stopAndStart()
        }
    }

    fun enchantEquipment(equipment: Equipment, scroll: EnchantScroll) {
        val enchantedEquipment = enchantEquipmentUseCase(
            item = equipment.toDomainModel(),
            scroll = scroll.toDomainModel(),
        )?.fromDomainModel()

        enchantedEquipment?.let {
            replaceOwnedItem(enchantedEquipment)
        } ?: run {
            removeItemOnOwnedItemList(equipment.uuid)
        }
    }

}