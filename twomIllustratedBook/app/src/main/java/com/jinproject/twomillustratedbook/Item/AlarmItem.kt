package com.jinproject.twomillustratedbook.Item

import java.util.*

data class AlarmItem(val name:String,val imgName:String,val code:Int,val gtime:Int){
    constructor():this("","",0,0)
}