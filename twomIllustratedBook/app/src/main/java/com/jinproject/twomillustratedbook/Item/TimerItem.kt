package com.jinproject.twomillustratedbook.Item

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TimerItem(var name:String,var day:Int,var hour:Int,var min:Int,var sec:Int){
    constructor():this("",1,1,1,1)
}
