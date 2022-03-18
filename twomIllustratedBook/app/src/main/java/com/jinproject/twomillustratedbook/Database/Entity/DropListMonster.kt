package com.jinproject.twomillustratedbook.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DropListMonster(var mons_name:String,var mons_level:Int,var mons_gtime:Int,@PrimaryKey val mons_Id:Int,var mons_imgName:String,var mons_type:String)
