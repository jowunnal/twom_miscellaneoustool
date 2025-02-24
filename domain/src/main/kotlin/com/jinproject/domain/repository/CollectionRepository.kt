package com.jinproject.domain.repository

import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionList(): Flow<List<CollectionModel>>

    fun getFilteredCollectionIds(): Flow<Set<Int>>

    suspend fun setFilteringCollections(collectionList: Set<Int>)

    suspend fun updateItemPrice(items: List<ItemModel>)

}