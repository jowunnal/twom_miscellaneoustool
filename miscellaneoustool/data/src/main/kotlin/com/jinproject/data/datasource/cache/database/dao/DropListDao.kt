package com.jinproject.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.jinproject.data.datasource.cache.database.entity.MonsDropItem
import com.jinproject.data.datasource.cache.database.entity.Monster
import kotlinx.coroutines.flow.Flow

@Dao
interface DropListDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Monster inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName where MonsLiveAtMap.mlMapName like:map order by Monster.monsLevel")
    fun getMonsterListFromMap(map: String): Flow<Map<Monster, List<MonsDropItem>>>

    @Query("select Maps.mapName, Maps.mapImgName, avg(Monster.monsLevel) as avgMonsLevel from Maps left join MonsLiveAtMap on Maps.mapName = MonsLiveAtMap.mlMapName left join Monster on MonsLiveAtMap.mlMonsName = Monster.monsName group by Maps.mapName order by avgMonsLevel")
    fun getMaps(): Flow<List<com.jinproject.data.datasource.cache.database.entity.Maps>>

    @Query("select * from Monster where Monster.monsType like:monsterType")
    fun getMonsterByType(monsterType: String): Flow<List<Monster>>

    @Query("select * from Monster where Monster.monsName like :monsterName")
    fun getMonsInfo(monsterName: String): Flow<Monster>
}