package com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item

import com.jinproject.twomillustratedbook.domain.model.MonsterType

data class MonsterState(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: MonsterType,
    val item: List<ItemState>
) {
    companion object {
        fun getInitValue() = MonsterState(
            name = "",
            level = 0,
            genTime = 0,
            imgName = "",
            type = MonsterType.NORMAL,
            item = emptyList()
        )
    }
}
