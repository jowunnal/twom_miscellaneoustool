package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.MapModel
import com.jinproject.data.repository.model.MonsterModel
import kotlinx.coroutines.flow.Flow

interface CacheDropListDataSource {
    fun getMaps(): Flow<List<MapModel>>
    fun getMonsterListFromMap(map: String): Flow<List<MonsterModel>>
    fun getAllMonsterList(): Flow<List<MonsterModel>>
    fun getMonsInfo(monsterName: String): Flow<MonsterModel>
}