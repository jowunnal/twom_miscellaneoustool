package com.jinproject.features.droplist.state

sealed interface Searchable : Comparable<Searchable> {
    val name: String
    val imageName: String
    val imagePrefix: String

    override fun compareTo(other: Searchable): Int = when (this) {
        is MonsterState -> when (other) {
            is MonsterState -> {
                val priorityCompare = this.type.getPriority().compareTo(other.type.getPriority())
                if (priorityCompare != 0) priorityCompare
                else {
                    val levelCompare = this.level.compareTo(other.level)
                    if (levelCompare != 0) levelCompare
                    else this.name.compareTo(other.name)
                }
            }
            is ItemState -> -1
        }
        is ItemState -> when (other) {
            is MonsterState -> 1
            is ItemState -> this.name.compareTo(other.name)
        }
    }
}
