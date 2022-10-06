package com.jinproject.twomillustratedbook.viewModel

import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Item.TimerItem
import com.jinproject.twomillustratedbook.Repository.DropListRepository
import com.jinproject.twomillustratedbook.Repository.DropListRepositoryImpl
import com.jinproject.twomillustratedbook.utils.getMonsterCode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DropListViewModel @Inject constructor(private val repository: DropListRepository, @ApplicationContext private val context:Context) : ViewModel() {
    var data_map=""
    var dataItemType=""
    var alarmItem = AlarmItem()
    private var clickable = -1
    lateinit var monster:Monster
    private var selectedBossListSharedPref = context.getSharedPreferences("bossList",Context.MODE_PRIVATE)
    var selectedBossList = MutableLiveData<List<String>>(selectedBossListSharedPref.getStringSet("boss", null)!!.toList())
    var selectedBossItem=""
    val droplistMaps = repository.getMaps()
    fun inputData(data:String) = repository.inputdata(data)
    fun getNameSp(inputData:String) = repository.getNameSp(inputData)

    suspend fun getMonsInfo(inputData: String):Monster{
        val info=viewModelScope.async(Dispatchers.IO){repository.getMonsInfo(inputData) }
        return info.await()
    }

    suspend fun checkIsClickedBoss(pos:Int,inputData: String){
        when (clickable) {
            -1 -> {
                clickable=pos
                monster=getMonsInfo(inputData)
                alarmItem=AlarmItem(monster.monsName,monster.monsImgName,
                    getMonsterCode(monster.monsName),monster.monsGtime)
            }
            pos -> {
                clickable=-1
            }
            else -> {
                Toast.makeText(context.applicationContext,"동시에 2개이상을 선택할수 없습니다. 해제후 다시선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getBossList(){
        viewModelScope.launch(Dispatchers.Main) {
            selectedBossList.value=selectedBossListSharedPref.getStringSet("boss", null)!!.toList()
        }
    }

    val bossItem:(String)->Unit = fun(item:String){
        selectedBossItem=item
    }

    fun setBossList(){
        val bossList=ArrayList<String>()
        bossList.addAll(selectedBossListSharedPref.getStringSet("boss", null)!!)
        bossList.add(selectedBossItem)
        selectedBossListSharedPref.edit().putStringSet("boss",bossList.toSet()).apply()
    }

}


