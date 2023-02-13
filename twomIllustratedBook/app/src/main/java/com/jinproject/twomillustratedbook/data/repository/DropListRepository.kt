package com.jinproject.twomillustratedbook.data.repository

import androidx.lifecycle.LiveData
import com.jinproject.twomillustratedbook.data.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.data.Entity.Monster
import com.jinproject.twomillustratedbook.domain.model.MapModel
import com.jinproject.twomillustratedbook.domain.model.MonsterModel
import kotlinx.coroutines.flow.Flow

interface DropListRepository {

    fun getMaps() : Flow<List<MapModel>>
    fun getMonsterListFromMap(map:String) : Flow<List<MonsterModel>>
    fun getNameSp(inputData:String) : LiveData<List<Monster>>
    suspend fun getMonsInfo(inputData:String) : Monster

}