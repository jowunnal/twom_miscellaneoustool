package com.jinproject.twomillustratedbook.Database.Dao

import androidx.room.*
import com.jinproject.twomillustratedbook.Item.User


@Dao
interface LoginDao {
    @Query("select * from User")
    suspend fun getUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user:User)

    @Delete
    suspend fun deleteUser(user:User)
}