package com.jinproject.domain.repository

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.TwomMap
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps(): Flow<List<TwomMap>>
    fun getMonsterListFromMap(map: String): Flow<List<Monster>>

    /**
     * @return 일반 등급을 제외한 몬스터들
     */
    fun getBossMonsterList(): Flow<List<Monster>>
    fun getMonsInfo(monsterName: String): Flow<Monster>

}