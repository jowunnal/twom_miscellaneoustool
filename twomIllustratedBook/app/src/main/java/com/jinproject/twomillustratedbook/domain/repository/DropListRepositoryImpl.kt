package com.jinproject.twomillustratedbook.domain.repository

import com.jinproject.twomillustratedbook.data.database.dao.DropListDao
import com.jinproject.twomillustratedbook.data.repository.DropListRepository
import com.jinproject.twomillustratedbook.domain.model.ItemModel
import com.jinproject.twomillustratedbook.domain.model.MapModel
import com.jinproject.twomillustratedbook.domain.model.MonsterModel
import com.jinproject.twomillustratedbook.domain.model.MonsterType
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