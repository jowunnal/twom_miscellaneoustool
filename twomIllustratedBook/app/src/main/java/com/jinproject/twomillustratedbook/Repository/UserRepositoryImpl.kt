package com.example.example_kakaologinapi.repository

import com.example.example_kakaologinapi.database.LoginDatabase
import com.example.example_kakaologinapi.database.User
import com.example.example_kakaologinapi.retrofit.DTO.RestaurantModule
import com.example.example_kakaologinapi.retrofit.DTO.RestaurantService
import com.example.example_kakaologinapi.retrofit.DTO.RfcOpenApi
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(val restaurantService: RestaurantService, val loginDatabase: LoginDatabase) : UserRepository {
    private val loginDao=loginDatabase.loginDao()
    override suspend fun getUser(): List<User> = loginDao.getUser()


    override suspend fun addUser(user: User) {
        loginDao.addUser(user)
    }

    override suspend fun deleteUser(user: User) {
        loginDao.deleteUser(user)
    }

    override suspend fun getRestaurantData(key: String, city: String): Response<RfcOpenApi> {
        return restaurantService.getData(key,city)
    }
}