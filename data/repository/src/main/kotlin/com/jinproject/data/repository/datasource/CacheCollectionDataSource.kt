package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.CollectionModel
import kotlinx.coroutines.flow.Flow

interface CacheCollectionDataSource {
    fun getFilteringCollectionList(): Flow<List<Int>>
    suspend fun setFilteringCollectionList(collectionList: List<Int>)
    suspend fun addFilteringCollection(id: Int)
    suspend fun updateItemPrice(name: String, price: Long)
    fun getCollectionItems(): Flow<List<CollectionModel>>
}
