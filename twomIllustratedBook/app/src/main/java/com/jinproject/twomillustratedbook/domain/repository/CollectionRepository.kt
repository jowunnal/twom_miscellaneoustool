package com.jinproject.twomillustratedbook.domain.repository

import com.jinproject.twomillustratedbook.domain.model.Category
import com.jinproject.twomillustratedbook.domain.model.CollectionModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun getCollectionList(category: Category): Flow<List<CollectionModel>>

}