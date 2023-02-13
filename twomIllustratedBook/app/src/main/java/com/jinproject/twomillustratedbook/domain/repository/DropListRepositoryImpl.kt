package com.jinproject.twomillustratedbook.domain.repository

import android.util.Log
import com.jinproject.twomillustratedbook.data.database.Dao.DropListDao
import com.jinproject.twomillustratedbook.data.repository.DropListRepository
import com.jinproject.twomillustratedbook.domain.model.ItemModel
import com.jinproject.twomillustratedbook.domain.model.MapModel
import com.jinproject.twomillustratedbook.domain.model.MonsterModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DropListRepositoryImpl @Inject constructor(private val dropListDao: DropListDao) :
    DropListRepository {

    override fun getMaps() = dropListDao.getMaps().map { maps ->
        maps.map { map ->
            MapModel.fromResponse(map)
        }
    }

    override fun getMonsterListFromMap(map: String) = dropListDao.getMonsterListFromMap(map)
        .map { response ->
            response.map { monster ->
                MonsterModel(
                    name = monster.key.monsName,
                    level = monster.key.monsLevel,
                    genTime = monster.key.monsGtime,
                    imgName = monster.key.monsImgName,
                    type = monster.key.monsType,
                    item = monster.value.map { item ->
                        ItemModel(item.mdItemName, 0)
                    }
                )
            }
        }

    override fun getNameSp(inputData: String) = dropListDao.getNamedSp(inputData)
    override suspend fun getMonsInfo(inputData: String) = dropListDao.getMonsInfo(inputData)

}