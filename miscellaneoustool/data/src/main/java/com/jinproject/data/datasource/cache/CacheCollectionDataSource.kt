package com.jinproject.data.datasource.cache

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface CacheCollectionDataSource {

    fun getFilteringCollectionList(): Flow<List<Int>>
    suspend fun setFilteringCollectionList(collectionList: List<Int>)
    suspend fun deleteFilter(id: Int)
    suspend fun setSymbolUri(uri: Uri)
    fun getSymbolUri(): Flow<List<String>>
}