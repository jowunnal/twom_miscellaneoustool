package com.miscellaneoustool.app.domain.repository

import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.domain.model.CollectionModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionList(category: Category): Flow<List<CollectionModel>>

    suspend fun deleteCollection(collectionList: List<Int>)

}