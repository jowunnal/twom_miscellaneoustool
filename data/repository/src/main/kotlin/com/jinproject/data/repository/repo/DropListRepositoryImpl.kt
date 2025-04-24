package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheDropListDataSource
import com.jinproject.data.repository.model.toDomainModel
import com.jinproject.data.repository.model.toDomainModels
import com.jinproject.domain.entity.Monster
import com.jinproject.domain.model.MapModel
import com.jinproject.domain.repository.DropListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DropListRepositoryImpl @Inject constructor(
    private val dropListDataSource: CacheDropListDataSource,
) : DropListRepository {

    override fun getMaps(): Flow<List<MapModel>> = dropListDataSource.getMaps().map { maps ->
        maps.map { map ->
            map.toDomainModel()
        }
    }

    override fun getMonsterListFromMap(map: String): Flow<List<Monster>> =
        dropListDataSource.getMonsterListFromMap(map)
            .map { monsterModels ->
                monsterModels.toDomainModels()
            }

    override fun getBossMonsterList(): Flow<List<Monster>> =
        dropListDataSource.getAllMonsterList().map {
            it.filter { it.type != "일반" && it.type != "Normal" }.toDomainModels()
        }

    override fun getMonsInfo(monsterName: String): Flow<Monster> =
        dropListDataSource.getMonsInfo(monsterName).map { monster ->
            monster.toMonsterDomain()
        }
}