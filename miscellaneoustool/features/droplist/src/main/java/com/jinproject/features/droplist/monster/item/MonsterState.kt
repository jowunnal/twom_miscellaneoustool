package com.jinproject.features.droplist.monster.item

data class MonsterState(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: com.jinproject.domain.model.MonsterType,
    val item: List<ItemState>
): Comparable<MonsterState> {
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

    override fun compareTo(other: MonsterState): Int =
        if(this.type.getPriority() == other.type.getPriority())
            this.level.compareTo(other.level)
        else
            this.type.getPriority().compareTo(other.type.getPriority())
}
