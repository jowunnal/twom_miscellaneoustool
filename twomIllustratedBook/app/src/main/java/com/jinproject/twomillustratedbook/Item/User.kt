package com.jinproject.twomillustratedbook.Item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var userName:String,@PrimaryKey  var userId:String, var userPw:String, var userAuthority:String){
    constructor():this("","","","")
}
