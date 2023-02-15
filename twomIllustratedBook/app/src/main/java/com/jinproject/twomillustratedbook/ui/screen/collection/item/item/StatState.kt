package com.jinproject.twomillustratedbook.ui.screen.collection.item.item

data class StatState(
    val name: String,
    val value: Double
) {
    companion object {
        fun fromDomain(name: String, value: Double) =
            StatState(
                name = name,
                value = value
            )

    }
}
