package com.jinproject.data.repository.repo

import com.jinproject.data.repository.datasource.CacheDropListDataSource
import com.jinproject.data.repository.model.toDomainModel
import com.jinproject.data.repository.model.toDomainModels
import com.jinproject.domain.model.MapModel
import com.jinproject.domain.model.MonsterModel
import com.jinproject.domain.model.MonsterType
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

    override fun getMonsterListFromMap(map: String): Flow<List<MonsterModel>> =
        dropListDataSource.getMonsterListFromMap(map)
            .map { monsterModels ->
                monsterModels.toDomainModels()
            }

    override fun getMonsterByType(monsterType: MonsterType): Flow<List<MonsterModel>> =
        dropListDataSource.getMonsterByType(monsterType.name).map { monsterModels ->
            monsterModels.toDomainModels()
        }

    override fun getMonsInfo(monsterName: String): Flow<MonsterModel> =
        dropListDataSource.getMonsInfo(monsterName).map { monster ->
            monster.toDomainModel()
        }
}