package com.jinproject.data.repository

import android.util.Log
import com.jinproject.data.datasource.cache.CacheSimulatorDataSource
import com.jinproject.data.datasource.cache.database.entity.toItemInfoDomainModel
import com.jinproject.data.model.toItemInfoDomainModel
import com.jinproject.domain.model.ItemInfo
import com.jinproject.domain.repository.SimulatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SimulatorRepositoryImpl @Inject constructor(
    private val cacheSimulatorDataSource: CacheSimulatorDataSource
) : SimulatorRepository {
    override fun getItemInfo(itemName: String): Flow<ItemInfo> =
        cacheSimulatorDataSource.getItemInfo(itemName).map { it.toItemInfoDomainModel() }

    override fun getAvailableItem(): Flow<List<ItemInfo>> =
        cacheSimulatorDataSource.getAvailableItem().map { equipmentList ->
            equipmentList.map { equipment ->
                val itemDetail = cacheSimulatorDataSource.getItemInfo(equipment.name).first()
                equipment.toItemInfoDomainModel(stat = itemDetail.stat)
            }
        }

    override fun getOwnedItems(): Flow<List<ItemInfo>> =
        cacheSimulatorDataSource.getOwnedItems().map { items ->
            items.map { item ->
                val itemDetail = cacheSimulatorDataSource.getItemInfo(item.name).first()

                ItemInfo(
                    name = item.name,
                    level = itemDetail.level,
                    stat = item.optionsMap,
                    imgName = itemDetail.imgName,
                    enchantNumber = item.enchantNumber,
                    uuid = item.uuid,
                )
            }
        }

    override suspend fun addItemOnOwnedItemList(itemInfo: ItemInfo) {
        cacheSimulatorDataSource.addItemOnOwnedItemList(
            com.jinproject.data.ItemInfo.getDefaultInstance().toBuilder().apply {
                name = itemInfo.name
                uuid = itemInfo.uuid
                enchantNumber = itemInfo.enchantNumber
                Log.d("test", "input options: ${itemInfo.stat.keys.toString()}")
                putAllOptions(itemInfo.stat)
            }.build()
        )
    }

    override suspend fun removeItemOnOwnedItemList(uuid: String) {
        cacheSimulatorDataSource.removeItemOnOwnedItemList(uuid)
    }

    override suspend fun replaceOwnedItem(item: ItemInfo) {
        cacheSimulatorDataSource.replaceOwnedItem(
            com.jinproject.data.ItemInfo.getDefaultInstance().toBuilder().apply {
                name = item.name
                uuid = item.uuid
                enchantNumber = item.enchantNumber
                putAllOptions(item.stat)
            }.build()
        )
    }

}