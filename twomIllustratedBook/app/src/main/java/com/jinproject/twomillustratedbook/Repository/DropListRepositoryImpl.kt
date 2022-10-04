package com.jinproject.twomillustratedbook.Repository

import androidx.lifecycle.LiveData
import com.jinproject.twomillustratedbook.Database.Dao.DropListDao
import com.jinproject.twomillustratedbook.Database.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import javax.inject.Inject

class DropListRepositoryImpl @Inject constructor(private val dropListDao: DropListDao) : DropListRepository{

    override fun getMaps() = dropListDao.getMaps()
    override fun inputdata(data: String) = dropListDao.getMonster(data)
    override fun getNameSp(inputData: String) = dropListDao.getNamedSp(inputData)
    override suspend fun getMonsInfo(inputData: String) = dropListDao.getMonsInfo(inputData)

}