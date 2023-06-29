package com.miscellaneoustool.domain.repository

import com.miscellaneoustool.domain.model.MapModel
import com.miscellaneoustool.domain.model.MonsterModel
import com.miscellaneoustool.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps() : Flow<List<MapModel>>
    fun getMonsterListFromMap(map:String) : Flow<List<MonsterModel>>
    fun getMonsterByType(monsterType: MonsterType) : Flow<List<MonsterModel>>
    fun getMonsInfo(monsterName:String) : Flow<MonsterModel>

}