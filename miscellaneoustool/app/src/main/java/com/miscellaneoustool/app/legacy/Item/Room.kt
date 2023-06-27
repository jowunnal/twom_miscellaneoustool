package com.miscellaneoustool.app.legacy.Item

data class Room(var roomId:String, var roomPw:String, var roomBossList: List<TimerItem>?, var authorityCode:String?){}