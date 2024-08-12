package com.jinproject.domain.repository

import com.jinproject.domain.model.MapModel
import com.jinproject.domain.model.MonsterModel
import com.jinproject.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps() : Flow<List<MapModel>>
    fun getMonsterListFromMap(map:String) : Flow<List<MonsterModel>>
    fun getMonsterByType(monsterType: MonsterType) : Flow<List<MonsterModel>>
    fun getMonsInfo(monsterName:String) : Flow<MonsterModel>

}