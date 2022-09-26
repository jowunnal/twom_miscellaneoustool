package com.jinproject.twomillustratedbook.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jinproject.twomillustratedbook.Database.Entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Book inner join RegisterItemToBook on Book.bookId = RegisterItemToBook.rlBookId join Item on Item.itemName = RegisterItemToBook.rlItemName where Item.itemType like:data")
    fun getContent(data:String): LiveData<Map<Book, List<RegisterItemToBook>>>

    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Monster inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName where MonsLiveAtMap.mlMapName like:data")
    fun getMonster(data :String) : LiveData<Map<Monster, List<MonsDropItem>>>

    @Query("select distinct mapName from Map")
    fun getMaps() : LiveData<List<String>>

    @Query("select * from Monster where Monster.monsType like:inputData")
    fun getNamedSp(inputData:String) : LiveData<List<Monster>>

    @Query("select * from Monster where Monster.monsName like :inputData")
    suspend fun getMonsInfo(inputData:String):Monster

    @Query("update Timer set day= :day,hour= :hour, min= :min,sec=:sec where timerMonsName like:name")
    suspend fun setTimer(day:Int,hour:Int,min:Int,sec:Int,name:String)

    @Query("select * from Timer")
    fun getTimer():LiveData<List<Timer>>

    @Query("delete from Timer where Timer.timerMonsName like :name")
    suspend fun deleteTimer(name:String)

    @Query("update Timer set ota=:ota where timerMonsName like :name")
    suspend fun setOta(ota:Int,name:String)

    /*@Query("select DropListMonster.mons_name from DropListMonster where DropListMonster.mons_type='boss'")
    fun getBossSp() : Map<String,Int>

    @Query("select DropListMonster.mons_name from DropListMonster where DropListMonster.mons_type='bigboss'")
    fun getBigBossSp() : Map<String,Int>*/

}