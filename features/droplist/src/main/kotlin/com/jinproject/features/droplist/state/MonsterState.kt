package com.jinproject.features.droplist.state

import com.jinproject.domain.model.MonsterType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MonsterState(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: MonsterType,
    val items: ImmutableList<String>
): Comparable<MonsterState> {
    companion object {
        fun getInitValue() = MonsterState(
            name = "",
            level = 0,
            genTime = 0,
            imgName = "",
            type = MonsterType.Normal("일반"),
            items = persistentListOf()
        )
    }

    override fun compareTo(other: MonsterState): Int =
        if(this.type.getPriority() == other.type.getPriority())
            if(this.level == other.level)
                this.name.compareTo(other.name)
            else
                this.level.compareTo(other.level)
        else
            this.type.getPriority().compareTo(other.type.getPriority())

    fun itemsToSingleLine() = items.joinToString(", ")

}
