package com.jinproject.twomillustratedbook.ui.viewModel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jinproject.twomillustratedbook.data.database.Entity.Timer
import com.jinproject.twomillustratedbook.domain.Item.Room
import com.jinproject.twomillustratedbook.domain.Item.TimerItem
import com.jinproject.twomillustratedbook.ui.Service.AlarmServerService
import com.jinproject.twomillustratedbook.ui.Service.WService
import com.jinproject.twomillustratedbook.utils.sortTimerList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ServerManageViewModel @Inject constructor(@ApplicationContext private val context:Context) : ViewModel() {
    var serverBossList = ArrayList<TimerItem>()
    var listToWservice= ArrayList<Timer>()
    private val loginPreference=context.getSharedPreferences("login",Context.MODE_PRIVATE)
    private val loginId=loginPreference.getString("id","")!!
    private val loginPw=loginPreference.getString("pw","")!!
    private val key=loginPreference.getString("key","")!!
    private val authorityCode=loginPreference.getString("authorityCode","")
    val serverStatue = "서버상태: ${loginPreference.getBoolean("statue",false)} / 로그인ID: $loginId"

    fun setServerBossList(timerList : List<Timer>){
        serverBossList.clear()
        for(item in timerList){
            serverBossList.add(TimerItem(item.timerMonsName,item.day,item.hour,item.min,item.sec))
        }
    }

    fun setTimerOtaList(timerList : List<Timer>){
        listToWservice.clear()
        for(item in timerList){
            if(item.ota==1){
                listToWservice.add(item)
            }
        }
        sortTimerList(listToWservice)

        if(listToWservice.isNotEmpty()){ //비어잇는게 아니면 백그라운드상에 동작하도록 서비스시작
            context.startService(Intent(context, WService::class.java).apply { putExtra("list",listToWservice) })
        }
        else{ // 비어잇으면(등록된타이머가없으면) 현재시간만 계속 출력하도록 함
            context.startService(Intent(context, WService::class.java))
        }
    }

    fun setServerOutput(){
        val db: DatabaseReference = FirebaseDatabase.getInstance().reference
        try {
            db.child("RoomList").child(key).get().addOnSuccessListener {
                if(it.child("authorityCode").value==authorityCode && serverBossList.isNotEmpty()){
                    db.child("RoomList").child(key).setValue(Room(loginId,loginPw,serverBossList,authorityCode))
                    Toast.makeText(context,"서버에 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"등록 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:kotlin.UninitializedPropertyAccessException){
            Toast.makeText(context,"먼저 보스를 선택해주세요!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setServerOnOff() : String{
        val serverStatue = when(loginPreference.getBoolean("statue",false)){
            false->{
                context.startService(Intent(context, AlarmServerService::class.java))
                loginPreference.edit().putBoolean("statue",true).apply()
                Toast.makeText(context,"서버로부터 데이터를 받기 시작합니다.",Toast.LENGTH_SHORT).show()
                "온라인"
            }
            true->{
                context.stopService(Intent(context, AlarmServerService::class.java))
                loginPreference.edit().putBoolean("statue",false).apply()
                Toast.makeText(context,"서버로부터 데이터를 받지 않습니다.",Toast.LENGTH_SHORT).show()
                "오프라인"
            }
        }
        return "서버상태: $serverStatue /로그인ID: $loginId"
    }
}