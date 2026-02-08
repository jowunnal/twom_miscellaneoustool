package com.jinproject.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.jinproject.data.datasource.cache.database.entity.Item
import com.jinproject.data.datasource.cache.database.entity.Monster
import kotlinx.coroutines.flow.Flow

@Dao
interface DropListDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        select *
        from Monster 
                    inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName
                    join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName
                    join Item on Item.itemName = MonsDropItem.mdItemName 
        where MonsLiveAtMap.mlMapName like:map order by Monster.monsLevel
    """)
    fun getMonsterListFromMap(map: String): Flow<Map<Monster, List<Item>>>

    @Query("select Maps.mapName, Maps.mapImgName, avg(Monster.monsLevel) as avgMonsLevel from Maps left join MonsLiveAtMap on Maps.mapName = MonsLiveAtMap.mlMapName left join Monster on MonsLiveAtMap.mlMonsName = Monster.monsName group by Maps.mapName order by avgMonsLevel")
    fun getMapList(): Flow<List<com.jinproject.data.datasource.cache.database.entity.Maps>>

    @Query("SELECT * FROM Monster ORDER BY Monster.monsLevel")
    fun getAllMonsterList(): Flow<List<Monster>>

    @Query("SELECT * FROM Monster WHERE Monster.monsName LIKE :monsterName")
    fun getMonsInfo(monsterName: String): Flow<Monster>

    @Query("""
        SELECT Maps.mapName, Maps.mapImgName, avg(Monster.monsLevel) as avgMonsLevel
        FROM Maps
        INNER JOIN MonsLiveAtMap ON Maps.mapName = MonsLiveAtMap.mlMapName
        INNER JOIN Monster ON MonsLiveAtMap.mlMonsName = Monster.monsName
        WHERE Monster.monsName LIKE '%' || :monsterName || '%'
        GROUP BY Maps.mapName
        ORDER BY avgMonsLevel
    """)
    fun getMapsByMonsterName(monsterName: String): Flow<List<com.jinproject.data.datasource.cache.database.entity.Maps>>


    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT * FROM Monster
        LEFT JOIN MonsDropItem ON Monster.monsName = MonsDropItem.mdMonsName
        LEFT JOIN Item ON Item.itemName = MonsDropItem.mdItemName
        ORDER BY Monster.monsLevel
    """)
    fun getMonsterListWithDropItemList(): Flow<Map<Monster, List<Item>>>

    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT * FROM Monster
        LEFT JOIN MonsLiveAtMap ON Monster.monsName = MonsLiveAtMap.mlMonsName
        LEFT JOIN Maps ON MonsLiveAtMap.mlMapName = Maps.mapName
        ORDER BY Monster.monsLevel
    """)
    fun getMonsterListWithMaps(): Flow<Map<Monster, List<com.jinproject.data.datasource.cache.database.entity.Maps>>>
}