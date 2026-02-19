package com.jinproject.features.home.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class ItemCollection(
    val id: Int,
    val stats: ImmutableMap<String, Float>,
    val items: ImmutableList<Item>,
)
