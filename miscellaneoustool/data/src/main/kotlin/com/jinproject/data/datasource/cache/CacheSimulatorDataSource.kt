package com.jinproject.data.datasource.cache

import com.jinproject.data.ItemInfo
import com.jinproject.data.datasource.cache.database.entity.Equipment
import com.jinproject.data.model.ItemDetail
import kotlinx.coroutines.flow.Flow

interface CacheSimulatorDataSource {
    fun getItemInfo(itemName: String): Flow<ItemDetail>
    fun getAvailableItem(): Flow<List<Equipment>>
    fun getOwnedItems(): Flow<List<com.jinproject.data.ItemInfo>>
    suspend fun addItemOnOwnedItemList(item: ItemInfo)
    suspend fun removeItemOnOwnedItemList(uuid: String)
    suspend fun replaceOwnedItem(item: ItemInfo)
}