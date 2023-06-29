package com.miscellaneoustool.app.data.datasource.cache.database.dao

import androidx.room.*
import com.miscellaneoustool.app.data.datasource.cache.database.entity.MonsDropItem
import com.miscellaneoustool.app.data.datasource.cache.database.entity.Monster
import kotlinx.coroutines.flow.Flow

@Dao
interface DropListDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Monster inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName where MonsLiveAtMap.mlMapName like:map")
    fun getMonsterListFromMap(map: String): Flow<Map<Monster, List<MonsDropItem>>>

    @Query("select distinct Map.mapName, Map.mapImgName from Map join MonsLiveAtMap on Map.mapName = MonsLiveAtMap.mlMapName join Monster on MonsLiveAtMap.mlMonsName = Monster.monsName order by Monster.monsLevel ")
    fun getMaps(): Flow<List<com.miscellaneoustool.app.data.datasource.cache.database.entity.Map>>

    @Query("select * from Monster where Monster.monsType like:monsterType")
    fun getMonsterByType(monsterType: String): Flow<List<Monster>>

    @Query("select * from Monster where Monster.monsName like :monsterName")
    fun getMonsInfo(monsterName: String): Flow<Monster>
}