package com.jinproject.features.collection.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

internal data class ItemCollection(
    val id: Int,
    val stats: ImmutableMap<String, Float>,
    val items: ImmutableList<Item>,
)