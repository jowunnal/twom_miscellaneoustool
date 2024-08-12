package com.jinproject.data.repository

import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.mapper.toItemModel
import com.jinproject.data.mapper.toMapModel
import com.jinproject.data.mapper.toMonsterModel
import com.jinproject.domain.model.MonsterModel
import com.jinproject.domain.model.MonsterType
import com.jinproject.domain.repository.DropListRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DropListRepositoryImpl @Inject constructor(private val dropListDao: DropListDao) :
    DropListRepository {

    override fun getMaps() = dropListDao.getMaps().map { maps ->
        maps.map { map ->
            map.toMapModel()
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
                    type = MonsterType.findByStoredName(monster.key.monsType),
                    item = monster.value.map { item ->
                        item.toItemModel()
                    }
                )
            }
        }

    override fun getMonsterByType(monsterType: MonsterType) =
        dropListDao.getMonsterByType(monsterType.storedName).map { response ->
            response.map { monster -> monster.toMonsterModel() }
        }

    override fun getMonsInfo(monsterName: String) =
        dropListDao.getMonsInfo(monsterName).map { monster ->
            monster.toMonsterModel()
        }

}