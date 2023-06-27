package com.miscellaneoustool.app.domain.model

data class CollectionModel(
    val bookId: Int,
    val stat: Map<Stat, Double>,
    val items: List<ItemModel>
)
