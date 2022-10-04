package com.jinproject.twomillustratedbook.Repository

import com.jinproject.twomillustratedbook.Database.Dao.CollectionDao
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(private val collectionDao: CollectionDao) : CollectionRepository {

    override fun bookList(data: String) = collectionDao.getContent(data)

}