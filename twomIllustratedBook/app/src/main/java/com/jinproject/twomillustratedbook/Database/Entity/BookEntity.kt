package com.jinproject.twomillustratedbook.Database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class BookEntity(var hp : Double,var mp : Double,var hp_per :Double,var mp_per: Double,var hp_regen: Double,var mp_regen: Double,
                      var hr: Double,var cri: Double,var stat_int: Double,var stat_str: Double,var stat_dex: Double,var move: Double,var armor: Double
                      ,var pve_dmg: Double,var pvp_dmg: Double, var pve_dmg_down: Double,var pvp_dmg_down: Double,var pve_dmg_down_per: Double,
                      var pvp_dmg_down_per: Double,var gold_drop: Double,var item_drop: Double,var pve_dmg_per: Double,var pvp_dmg_per: Double
                      ,@PrimaryKey  var id:Int, var item_type:String)
