package com.jinproject.twomillustratedbook.data.repository

import com.jinproject.twomillustratedbook.data.database.dao.CollectionDao
import com.jinproject.twomillustratedbook.domain.model.CollectionModel
import com.jinproject.twomillustratedbook.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(private val collectionDao: CollectionDao) :
    CollectionRepository {

    override fun getCollectionList(data: String): Flow<List<CollectionModel>> =
        collectionDao.getCollectionList(data).map { response ->
            response.map { collection ->
                CollectionModel.fromCollectionResponse(collection)
            }
        }

}