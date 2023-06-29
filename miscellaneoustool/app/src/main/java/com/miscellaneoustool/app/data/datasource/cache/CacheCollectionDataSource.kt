package com.miscellaneoustool.app.data.datasource.cache

import kotlinx.coroutines.flow.Flow

interface CacheCollectionDataSource {

    fun getFilteringCollectionList(): Flow<List<Int>>
    suspend fun setFilteringCollectionList(collectionList: List<Int>)
    suspend fun deleteFilter(id: Int)
}