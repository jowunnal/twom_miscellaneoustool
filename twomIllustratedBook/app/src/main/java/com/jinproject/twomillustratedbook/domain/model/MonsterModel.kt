package com.jinproject.twomillustratedbook.domain.model

import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.MonsterState

data class MonsterModel(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: String,
    val item: List<ItemModel>
) {
    fun toMonsterState() = MonsterState(
        name = name,
        level = level,
        genTime = genTime,
        imgName = imgName,
        type = MonsterType.findByStoredName(type),
        item = item.map { itemModel ->
            itemModel.toItemState()
        }
    )
}
