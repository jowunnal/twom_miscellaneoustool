package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheSimulatorDataSource
import com.jinproject.data.repository.model.toEquipmentData
import com.jinproject.data.repository.model.toItemInfoDomainModel
import com.jinproject.data.repository.model.toItemInfoListDomainModel
import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.Equipment
import com.jinproject.domain.repository.SimulatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SimulatorRepositoryImpl @Inject constructor(
    private val cacheSimulatorDataSource: CacheSimulatorDataSource,
) : SimulatorRepository {
    override fun getItemInfo(itemName: String): Flow<Equipment> =
        cacheSimulatorDataSource.getItemInfo(itemName).map { it.toItemInfoDomainModel() }

    override fun getEnchantableItems(): Flow<List<Equipment>> =
        cacheSimulatorDataSource.getItemInfos().map { equipmentList ->
            equipmentList.toItemInfoListDomainModel()
        }

    override fun getOwnedItems(): Flow<List<Equipment>> =
        cacheSimulatorDataSource.getOwnedItems().map { items ->
            items.map { item ->
                val info = cacheSimulatorDataSource.getItemInfo(item.name).first()

                item.copy(
                    level = info.level,
                    factory = info.factory,
                ).toItemInfoDomainModel()
            }
        }

    override suspend fun addItemOnOwnedItemList(equipment: EnchantableEquipment) {
        cacheSimulatorDataSource.addItemOnOwnedItemList(
            equipment.toEquipmentData()
        )
    }

    override suspend fun removeItemOnOwnedItemList(uuid: String) {
        cacheSimulatorDataSource.removeItemOnOwnedItemList(uuid)
    }

    override suspend fun replaceOwnedItem(equipment: EnchantableEquipment) {
        cacheSimulatorDataSource.replaceOwnedItem(
            equipment.toEquipmentData()
        )
    }

    override suspend fun updateOwnedItem(items: List<EnchantableEquipment>) {
        cacheSimulatorDataSource.updateOwnedItems(
            items.map { item ->
                item.toEquipmentData()
            })
    }
}