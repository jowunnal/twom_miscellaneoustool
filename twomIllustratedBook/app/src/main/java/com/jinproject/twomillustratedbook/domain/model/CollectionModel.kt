package com.jinproject.twomillustratedbook.domain.model

import com.jinproject.twomillustratedbook.data.Entity.Book
import com.jinproject.twomillustratedbook.data.Entity.RegisterItemToBook
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionState
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.StatState

data class CollectionModel(
    val bookId: Int,
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
    val items: List<ItemModel>
) {
    companion object {
        fun fromCollectionResponse(response: Map.Entry<Book, List<RegisterItemToBook>>) =
            CollectionModel(
                bookId = response.key.bookId,
                hp = response.key.hp,
                mp = response.key.mp,
                hpPer = response.key.hpPer,
                mpPer = response.key.mpPer,
                hpRegen = response.key.hpRegen,
                mpRegen = response.key.mpRegen,
                hr = response.key.hr,
                cri = response.key.cri,
                statInt = response.key.statInt,
                statStr = response.key.statStr,
                statDex = response.key.statDex,
                move = response.key.move,
                armor = response.key.armor,
                pveDmg = response.key.pveDmg,
                pvpDmg = response.key.pvpDmg,
                pveDmgPer = response.key.pveDmgPer,
                pvpDmgPer = response.key.pvpDmgPer,
                pveDmgDown = response.key.pveDmgDown,
                pvpDmgDown = response.key.pvpDmgDown,
                pveDmgDownPer = response.key.pveDmgDownPer,
                pvpDmgDownPer = response.key.pvpDmgDownPer,
                goldDrop = response.key.goldDrop,
                itemDrop = response.key.itemDrop,
                items = response.value.map { item -> ItemModel.fromRegisterItemToDomain(item) }
            )
    }
}
