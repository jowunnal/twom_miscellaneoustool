package com.jinproject.twomillustratedbook.data.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jinproject.twomillustratedbook.data.Entity.*
import com.jinproject.twomillustratedbook.data.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.data.Entity.Monster
import kotlinx.coroutines.flow.Flow

@Dao
interface DropListDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Monster inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName where MonsLiveAtMap.mlMapName like:map")
    fun getMonsterListFromMap(map: String): Flow<Map<Monster, List<MonsDropItem>>>

    @Query("select * from Map")
    fun getMaps(): Flow<List<com.jinproject.twomillustratedbook.data.Entity.Map>>

    @Query("select * from Monster where Monster.monsType like:inputData")
    fun getNamedSp(inputData: String): LiveData<List<Monster>>

    @Query("select * from Monster where Monster.monsName like :inputData")
    suspend fun getMonsInfo(inputData: String): Monster
}