package com.jinproject.twomillustratedbook.Database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = DropListMonster::class, parentColumns = ["mons_Id"], childColumns = ["map_monsId"])],indices = [Index("map_monsId")])
data class DropListMaps (var map_name:String,var map_monsId:Int,@PrimaryKey var map_Id :Int)
