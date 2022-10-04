package com.jinproject.twomillustratedbook.Repository

import androidx.lifecycle.LiveData
import com.jinproject.twomillustratedbook.Database.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.Database.Entity.Monster

interface DropListRepository {

    fun getMaps() : LiveData<List<String>>
    fun inputdata(data:String) : LiveData<Map<Monster, List<MonsDropItem>>>
    fun getNameSp(inputData:String) : LiveData<List<Monster>>
    suspend fun getMonsInfo(inputData:String) : Monster

}