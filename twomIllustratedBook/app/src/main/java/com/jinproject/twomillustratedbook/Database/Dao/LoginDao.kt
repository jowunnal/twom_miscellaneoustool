package com.example.example_kakaologinapi.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface LoginDao {
    @Query("select * from User")
    suspend fun getUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user:User)

    @Delete
    suspend fun deleteUser(user:User)
}