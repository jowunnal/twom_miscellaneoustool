package com.miscellaneoustool.app.ui.screen.droplist.monster.item

import com.miscellaneoustool.app.domain.model.MonsterType

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
