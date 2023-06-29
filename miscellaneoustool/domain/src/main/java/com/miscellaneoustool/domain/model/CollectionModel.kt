package com.miscellaneoustool.domain.model

data class CollectionModel(
    val bookId: Int,
    val stat: Map<Stat, Double>,
    val items: List<ItemModel>
)
