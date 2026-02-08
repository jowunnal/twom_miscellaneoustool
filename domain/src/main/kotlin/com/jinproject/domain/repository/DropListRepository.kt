package com.jinproject.domain.repository

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.TwomMap
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMapList(): Flow<List<TwomMap>>

    /** 기본 정보만 (아이템, 맵 없음) */
    fun getMonsterList(): Flow<List<Monster>>

    /** 아이템 정보 포함 */
    fun getMonsterListWithItems(): Flow<List<Monster>>

    /** 아이템 + 맵 정보 모두 포함 */
    fun getMonsterListComplete(): Flow<List<Monster>>

    /** 기본 정보만 */
    fun getMonster(name: String): Flow<Monster>
}