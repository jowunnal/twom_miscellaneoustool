package com.jinproject.data.datasource.cache

import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.datasource.cache.database.entity.toItemDataModelList
import com.jinproject.data.datasource.cache.database.entity.toMapDataModel
import com.jinproject.data.datasource.cache.database.entity.toMonsterModel
import com.jinproject.data.repository.datasource.CacheDropListDataSource
import com.jinproject.data.repository.model.MapModel
import com.jinproject.data.repository.model.MonsterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CacheDropListDataSourceImpl @Inject constructor(
    private val dropListDao: DropListDao,
) : CacheDropListDataSource {

    override fun getMaps(): Flow<List<MapModel>> = dropListDao.getMaps().map { maps ->
        maps.map { map ->
            map.toMapDataModel()
        }
    }

    override fun getMonsterListFromMap(map: String): Flow<List<MonsterModel>> =
        dropListDao.getMonsterListFromMap(map).map { response ->
            response.map { monster ->
                MonsterModel(
                    name = monster.key.monsName,
                    level = monster.key.monsLevel,
                    genTime = monster.key.monsGtime,
                    imgName = monster.key.monsImgName,
                    type = monster.key.monsType,
                    item = monster.value.toItemDataModelList()
                )
            }
        }

    override fun getAllMonsterList(): Flow<List<MonsterModel>> =
        dropListDao.getAllMonsterList().map { it.map { it.toMonsterModel() } }

    override fun getMonsInfo(monsterName: String): Flow<MonsterModel> =
        dropListDao.getMonsInfo(monsterName).map { monster ->
            monster.toMonsterModel()
        }
}