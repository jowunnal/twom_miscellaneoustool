package com.jinproject.domain.entity

import com.jinproject.domain.entity.item.Item

data class ItemCollection(
    val id: Int,
    val stats: Map<String, Float>,
    val requiredItems: List<Item>,
)
