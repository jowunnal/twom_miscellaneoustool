package com.jinproject.twomillustratedbook.domain.repository

import com.jinproject.twomillustratedbook.domain.model.MapModel
import com.jinproject.twomillustratedbook.domain.model.MonsterModel
import com.jinproject.twomillustratedbook.domain.model.MonsterType
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps() : Flow<List<MapModel>>
    fun getMonsterListFromMap(map:String) : Flow<List<MonsterModel>>
    fun getMonsterByType(monsterType: MonsterType) : Flow<List<MonsterModel>>
    fun getMonsInfo(monsterName:String) : Flow<MonsterModel>

}