package com.jinproject.twomillustratedbook.data.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val bookId: Int,
    val hp: Double,
    val mp: Double,
    val hpPer: Double,
    val mpPer: Double,
    val hpRegen: Double,
    val mpRegen: Double,
    val hr: Double,
    val cri: Double,
    val statInt: Double,
    val statStr: Double,
    val statDex: Double,
    val move: Double,
    val armor: Double,
    val pveDmg: Double,
    val pvpDmg: Double,
    val pveDmgPer: Double,
    val pvpDmgPer: Double,
    val pveDmgDown: Double,
    val pvpDmgDown: Double,
    val pveDmgDownPer: Double,
    val pvpDmgDownPer: Double,
    val goldDrop: Double,
    val itemDrop: Double,
    val bossDmgPer: Double,
    val critDmgDown: Double,
    val critDmgDownPer: Double,
    val miss: Double,
    val critResistPer: Double
)