package com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item

data class MonsterState(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: com.jinproject.domain.model.MonsterType,
    val item: List<ItemState>
) {
    companion object {
        fun getInitValue() = MonsterState(
            name = "",
            level = 0,
            genTime = 0,
            imgName = "",
            type = com.jinproject.domain.model.MonsterType.NORMAL,
            item = emptyList()
        )
    }
}
