package com.jinproject.twomillustratedbook.Item

import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import com.jinproject.twomillustratedbook.Database.Entity.Timer
import com.jinproject.twomillustratedbook.Repository.BookRepository
import com.jinproject.twomillustratedbook.Repository.BookRepositoryImpl
import com.jinproject.twomillustratedbook.Repository.BookRepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepositoryImpl) : ViewModel() {
    var data_map=""
    var dataItemType=""
    fun content(data:String) = repository.bookList(data)
    val droplistMaps:LiveData<List<String>> = repository.maps
    fun inputData(data:String) = repository.inputdata(data)
    fun getNameSp(inputData:String) = repository.getNameSp(inputData)
    fun setTimer(day:Int,hour:Int,min:Int,sec:Int,name:String,statue:Int)=viewModelScope.launch(Dispatchers.IO){repository.setTimer(day,hour, min,sec, name)}
    val timer:LiveData<List<Timer>> = repository.timer
    fun setOta(ota:Int,name:String) = viewModelScope.launch(Dispatchers.IO) {  repository.setOta(ota,name)}
    suspend fun getMonsInfo(inputData: String):Monster{
        val info=viewModelScope.async(Dispatchers.IO){repository.getMonsInfo(inputData) }
        return info.await()
    }
    /*fun getBossSp()=repository.getBossSp()
    fun getBigBossSp()=repository.getBigBossSp()*/

}


