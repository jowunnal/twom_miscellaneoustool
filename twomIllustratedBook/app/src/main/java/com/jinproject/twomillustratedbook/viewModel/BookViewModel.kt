package com.jinproject.twomillustratedbook.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Item.TimerItem
import com.jinproject.twomillustratedbook.Repository.DropListRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: DropListRepositoryImpl, @ApplicationContext private val context:Context) : ViewModel() {
    var data_map=""
    var dataItemType=""
    var alarmItem = AlarmItem()
    private var clickable = -1
    lateinit var monster:Monster
    fun content(data:String) = repository.bookList(data)
    val droplistMaps = repository.getMaps()
    fun inputData(data:String) = repository.inputdata(data)
    fun getNameSp(inputData:String) = repository.getNameSp(inputData)
    fun setTimer(timerItem: TimerItem)=viewModelScope.launch(Dispatchers.IO){repository.setTimer(timerItem.day,timerItem.hour,timerItem.min,timerItem.sec,timerItem.name)}
    val timer = repository.getTimer()
    fun setOta(ota:Int,name:String) = viewModelScope.launch(Dispatchers.IO) {  repository.setOta(ota,name)}
    suspend fun getMonsInfo(inputData: String):Monster{
        val info=viewModelScope.async(Dispatchers.IO){repository.getMonsInfo(inputData) }
        return info.await()
    }

    fun checkIsClickedBoss(pos:Int,inputData: String){
        when (clickable) {
            -1 -> {
                clickable=pos
                CoroutineScope(Dispatchers.IO).launch {
                    monster=getMonsInfo(inputData)
                    alarmItem=AlarmItem(monster.monsName,monster.monsImgName,monster.monsName.toInt(),monster.monsGtime)
                }
            }
            pos -> {
                clickable=-1
            }
            else -> {
                Toast.makeText(context.applicationContext,"동시에 2개이상을 선택할수 없습니다. 해제후 다시선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


