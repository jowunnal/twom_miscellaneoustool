package com.jinproject.twomillustratedbook.Item

data class Room(var roomId:String, var roomPw:String, var roomBossList: List<TimerItem>?,var authorityCode:String?){
    constructor():this("","", listOf(TimerItem("",0,0,0,0)),"")
}