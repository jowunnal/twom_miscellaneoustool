package com.jinproject.twomillustratedbook.Item

import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Database.Entity.DropListMonster
import com.jinproject.twomillustratedbook.Database.Entity.Timer
import com.jinproject.twomillustratedbook.Repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    var data_map=""
    var dataItemType=""
    fun content(data:String) = repository.bookList(data)
    val droplistMaps:LiveData<List<String>> = repository.maps
    fun inputData(data:String) = repository.inputdata(data)
    fun getNameSp(inputData:String) = repository.getNameSp(inputData)
    fun setTimer(day:Int,hour:Int,min:Int,sec:Int,name:String,statue:Int)=viewModelScope.launch(Dispatchers.IO){repository.setTimer(day,hour, min,sec, name,statue)}
    val timer:LiveData<List<Timer>> = repository.timer
    fun setOta(ota:Int,name:String) = viewModelScope.launch(Dispatchers.IO) {  repository.setOta(ota,name)}
    suspend fun getMonsInfo(inputData: String):DropListMonster{
        val info=viewModelScope.async(Dispatchers.IO){repository.getMonsInfo(inputData) }
        return info.await()
    }
    /*fun getBossSp()=repository.getBossSp()
    fun getBigBossSp()=repository.getBigBossSp()*/

}


class BookViewModelFactory(private val repositorys:BookRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repositorys) as T
        }
        throw IllegalArgumentException("unknown Viewmodel class")
    }

}


