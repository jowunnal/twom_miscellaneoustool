package com.jinproject.domain.repository

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.model.MapModel
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps(): Flow<List<MapModel>>
    fun getMonsterListFromMap(map: String): Flow<List<Monster>>

    /**
     * @return 일반 등급을 제외한 몬스터들
     */
    fun getBossMonsterList(): Flow<List<Monster>>
    fun getMonsInfo(monsterName: String): Flow<Monster>

}