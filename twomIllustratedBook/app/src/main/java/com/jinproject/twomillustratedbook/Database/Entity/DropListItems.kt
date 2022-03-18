package com.jinproject.twomillustratedbook.Database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = DropListMonster::class, parentColumns = ["mons_Id"], childColumns = ["monsId"])],indices = [Index("monsId")])
data class DropListItems(var item_name:String,var monsId:Int,@PrimaryKey var item_Id:Int)
