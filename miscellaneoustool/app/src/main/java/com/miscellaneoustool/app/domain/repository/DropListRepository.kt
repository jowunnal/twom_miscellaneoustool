package com.miscellaneoustool.app.domain.repository

import com.miscellaneoustool.app.domain.model.MapModel
import com.miscellaneoustool.app.domain.model.MonsterModel
import com.miscellaneoustool.app.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps() : Flow<List<MapModel>>
    fun getMonsterListFromMap(map:String) : Flow<List<MonsterModel>>
    fun getMonsterByType(monsterType: MonsterType) : Flow<List<MonsterModel>>
    fun getMonsInfo(monsterName:String) : Flow<MonsterModel>

}