package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.datasource.base.CacheDataStoreDataSource
import com.jinproject.data.repository.model.Equipment
import com.jinproject.data.repository.model.ItemDetail
import com.jinproject.data.repository.model.ItemInfo
import kotlinx.coroutines.flow.Flow

interface CacheSimulatorDataSource<T> : CacheDataStoreDataSource<T> {
    fun getItemInfo(itemName: String): Flow<ItemDetail>
    fun getAvailableItem(): Flow<List<Equipment>>
    fun getOwnedItems(): Flow<List<ItemInfo>>
    suspend fun addItemOnOwnedItemList(item: ItemInfo)
    suspend fun removeItemOnOwnedItemList(uuid: String)
    suspend fun replaceOwnedItem(item: ItemInfo)
}
