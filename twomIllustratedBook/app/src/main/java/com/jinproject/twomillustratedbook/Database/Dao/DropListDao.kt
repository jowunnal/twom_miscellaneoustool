package com.jinproject.twomillustratedbook.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jinproject.twomillustratedbook.Database.Entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DropListDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Monster inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName where MonsLiveAtMap.mlMapName like:data")
    fun getMonster(data :String) : LiveData<Map<Monster, List<MonsDropItem>>>

    @Query("select distinct mapName from Map")
    fun getMaps() : LiveData<List<String>>

    @Query("select * from Monster where Monster.monsType like:inputData")
    fun getNamedSp(inputData:String) : LiveData<List<Monster>>

    @Query("select * from Monster where Monster.monsName like :inputData")
    suspend fun getMonsInfo(inputData:String):Monster
}