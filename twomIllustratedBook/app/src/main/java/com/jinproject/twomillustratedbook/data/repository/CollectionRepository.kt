package com.jinproject.twomillustratedbook.data.repository

import com.jinproject.twomillustratedbook.domain.model.CollectionModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun getCollectionList(data:String): Flow<List<CollectionModel>>

}