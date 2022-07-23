package com.jinproject.twomillustratedbook.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jinproject.twomillustratedbook.Database.Entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("select * from BookEntity inner join BookContentEntity on BookEntity.id = BookContentEntity.bookId where item_type like:data")
    fun getContent(data:String): LiveData<Map<BookEntity, List<BookContentEntity>>>

    @Query("select * from DropListMonster inner join DropListItems on DropListMonster.mons_Id=DropListItems.monsId inner join DropListMaps on DropListMonster.mons_Id=DropListMaps.map_monsId where DropListMaps.map_name like:data")
    fun getMonster(data :String) : LiveData<Map<DropListMonster, List<DropListItems>>>

    @Query("select distinct map_name from DropListMaps")
    fun getMaps() : LiveData<List<String>>

    @Query("select * from DropListMonster where DropListMonster.mons_type like:inputData")
    fun getNamedSp(inputData:String) : LiveData<List<DropListMonster>>

    @Query("select * from DropListMonster where mons_name like :inputData")
    suspend fun getMonsInfo(inputData:String):DropListMonster

    @Query("update Timer set timer_day= :day,timer_hour= :hour, timer_min= :min,timer_sec=:sec, timer_statue= :statue where timer_name like:name")
    suspend fun setTimer(day:Int,hour:Int,min:Int,sec:Int,name:String,statue:Int)

    @Query("select * from Timer where timer_statue!=0")
    fun getTimer():LiveData<List<Timer>>

    @Query("update Timer set timer_ota=:ota where timer_name like :name")
    suspend fun setOta(ota:Int,name:String)

    /*@Query("select DropListMonster.mons_name from DropListMonster where DropListMonster.mons_type='boss'")
    fun getBossSp() : Map<String,Int>

    @Query("select DropListMonster.mons_name from DropListMonster where DropListMonster.mons_type='bigboss'")
    fun getBigBossSp() : Map<String,Int>*/

    @Query("delete from BookEntity")
    suspend fun deleteAll()

    @Query("select * from BookEntity")
    fun getAll(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookEntity: BookEntity)

}