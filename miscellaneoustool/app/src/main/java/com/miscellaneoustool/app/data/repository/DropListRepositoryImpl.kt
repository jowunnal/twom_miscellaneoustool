package com.miscellaneoustool.app.data.repository

import com.miscellaneoustool.app.data.datasource.cache.database.dao.DropListDao
import com.miscellaneoustool.app.domain.model.ItemModel
import com.miscellaneoustool.app.domain.model.MapModel
import com.miscellaneoustool.app.domain.model.MonsterModel
import com.miscellaneoustool.app.domain.model.MonsterType
import com.miscellaneoustool.app.domain.repository.DropListRepository
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
                    type = MonsterType.findByStoredName(monster.key.monsType),
                    item = monster.value.map { item ->
                        ItemModel.fromMonsDropItemToDomain(item)
                    }
                )
            }
        }

    override fun getMonsterByType(monsterType: MonsterType) =
        dropListDao.getMonsterByType(monsterType.storedName).map { response ->
            response.map { monster -> MonsterModel.fromMonsterResponse(monster) }
        }

    override fun getMonsInfo(monsterName: String) =
        dropListDao.getMonsInfo(monsterName).map { response ->
            MonsterModel.fromMonsterResponse(response)
        }

}