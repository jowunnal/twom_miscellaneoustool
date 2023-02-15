package com.jinproject.twomillustratedbook.ui.screen.collection.item.item

import com.jinproject.twomillustratedbook.domain.model.CollectionModel

data class CollectionState(
    val stats: List<StatState>,
    val items: List<CollectionItemState>
) {
    companion object {
        fun fromCollectionModel(collectionModel: CollectionModel) = CollectionState(
            stats = mutableListOf<StatState>().apply {
                add(StatState(value = collectionModel.hp, name = "체력"))
                add(StatState(value = collectionModel.mp, name = "마나"))
                add(StatState(value = collectionModel.hpPer, name = "체력퍼센트"))
                add(StatState(value = collectionModel.mpPer, name = "마나퍼센트"))
                add(StatState(value = collectionModel.hpRegen, name = "체력재생"))
                add(StatState(value = collectionModel.mpRegen, name = "마나재생"))
                add(StatState(value = collectionModel.hr, name = "명중"))
                add(StatState(value = collectionModel.cri, name = "크리티컬"))
                add(StatState(value = collectionModel.statInt, name = "지능"))
                add(StatState(value = collectionModel.statStr, name = "힘"))
                add(StatState(value = collectionModel.statDex, name = "민첩"))
                add(StatState(value = collectionModel.move, name = "이동속도"))
                add(StatState(value = collectionModel.armor, name = "방어력"))
                add(StatState(value = collectionModel.pveDmg, name = "pve데미지"))
                add(StatState(value = collectionModel.pvpDmg, name = "pvp데미지"))
                add(StatState(value = collectionModel.pveDmgPer, name = "pve데미지퍼센트"))
                add(StatState(value = collectionModel.pvpDmgPer, name = "pvp데미지퍼센트"))
                add(StatState(value = collectionModel.pveDmgDown, name = "pve데미지감소"))
                add(StatState(value = collectionModel.pvpDmgDown, name = "pvp데미지감소"))
                add(StatState(value = collectionModel.pveDmgDownPer, name = "pve데미지감소퍼센트"))
                add(StatState(value = collectionModel.pvpDmgDownPer, name = "pvp데미지감소퍼센트"))
                add(StatState(value = collectionModel.goldDrop, name = "골드드랍률"))
                add(StatState(value = collectionModel.itemDrop, name = "아이템드랍률"))
            },
            items = collectionModel.items.map { item -> item.toCollectionItemState() }
        )
    }
}

