package com.jinproject.twomillustratedbook.domain.model

import com.jinproject.twomillustratedbook.data.database.Entity.Book
import com.jinproject.twomillustratedbook.data.database.Entity.RegisterItemToBook

data class CollectionModel(
    val bookId: Int,
    val stat: Map<Stat, Double>,
    val items: List<ItemModel>
) {
    companion object {
        fun fromCollectionResponse(response: Map.Entry<Book, List<RegisterItemToBook>>) =
            CollectionModel(
                bookId = response.key.bookId,
                stat = mutableMapOf<Stat, Double>().apply {
                    put(Stat.Hp, response.key.hp)
                    put(Stat.Mp, response.key.mp)
                    put(Stat.HpPer, response.key.hpPer)
                    put(Stat.MpPer, response.key.mpPer)
                    put(Stat.HpRegen, response.key.hpRegen)
                    put(Stat.MpRegen, response.key.mpRegen)
                    put(Stat.Hr, response.key.hr)
                    put(Stat.Cri, response.key.cri)
                    put(Stat.StatInt, response.key.statInt)
                    put(Stat.StatStr, response.key.statStr)
                    put(Stat.StatDex, response.key.statDex)
                    put(Stat.Move, response.key.move)
                    put(Stat.Armor, response.key.armor)
                    put(Stat.PveDmg, response.key.pveDmg)
                    put(Stat.PvpDmg, response.key.pvpDmg)
                    put(Stat.PveDmgPer, response.key.pveDmgPer)
                    put(Stat.PvpDmgPer, response.key.pvpDmgPer)
                    put(Stat.PveDmgDown, response.key.pveDmgDown)
                    put(Stat.PvpDmgDown, response.key.pvpDmgDown)
                    put(Stat.PveDmgDownPer, response.key.pveDmgDownPer)
                    put(Stat.PvpDmgDownPer, response.key.pvpDmgDownPer)
                    put(Stat.GoldDrop, response.key.goldDrop)
                    put(Stat.ItemDrop, response.key.itemDrop)
                    put(Stat.BossDmgPer, response.key.bossDmgPer)
                    put(Stat.CriDmgDown, response.key.critDmgDown)
                    put(Stat.CriDmgDownPer, response.key.critDmgDownPer)
                    put(Stat.Miss, response.key.miss)
                    put(Stat.CriResistPer, response.key.critResistPer)
                }.filter { it.value != 0.0 },
                items = response.value.map { item -> ItemModel.fromRegisterItemToDomain(item) }
            )
    }
}
