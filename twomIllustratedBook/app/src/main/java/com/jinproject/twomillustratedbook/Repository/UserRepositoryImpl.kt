package com.jinproject.twomillustratedbook.Repository

import com.jinproject.twomillustratedbook.Database.Dao.LoginDao
import com.jinproject.twomillustratedbook.Item.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(val loginDao: LoginDao){

    suspend fun getUser(): List<User> = loginDao.getUser()


    suspend fun addUser(user: User) {
        loginDao.addUser(user)
    }

    suspend fun deleteUser(user: User) {
        loginDao.deleteUser(user)
    }
}