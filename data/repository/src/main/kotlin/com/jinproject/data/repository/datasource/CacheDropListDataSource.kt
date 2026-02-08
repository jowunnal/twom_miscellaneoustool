package com.jinproject.data.repository.datasource

import com.jinproject.data.repository.model.MapModel
import com.jinproject.data.repository.model.MonsterModel
import com.jinproject.data.repository.model.MonsterWithItemsModel
import com.jinproject.data.repository.model.MonsterWithMapsModel
import kotlinx.coroutines.flow.Flow

interface CacheDropListDataSource {
    fun getMapList(): Flow<List<MapModel>>
    fun getMapListByMonster(monsterName: String): Flow<List<MapModel>>

    fun getMonsterList(): Flow<List<MonsterModel>>
    fun getMonsterListWithItems(): Flow<List<MonsterWithItemsModel>>
    fun getMonsterListWithMaps(): Flow<List<MonsterWithMapsModel>>

    fun getMonster(name: String): Flow<MonsterModel>
    fun getMonsterListWithItemsByMap(map: String): Flow<List<MonsterWithItemsModel>>
}