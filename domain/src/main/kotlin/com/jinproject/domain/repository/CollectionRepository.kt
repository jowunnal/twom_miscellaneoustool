package com.jinproject.domain.repository

import com.jinproject.domain.model.ItemCollection
import com.jinproject.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun getCollectionList(): Flow<List<ItemCollection>>
    fun getFilteredCollectionIds(): Flow<List<Int>>
    suspend fun setFilteringCollections(collectionList: List<Int>)
    suspend fun addFilteringCollection(id: Int)
    suspend fun updateItemPrice(items: List<ItemModel>)

}