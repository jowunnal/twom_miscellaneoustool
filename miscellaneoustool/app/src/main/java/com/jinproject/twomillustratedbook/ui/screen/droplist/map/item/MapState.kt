package com.jinproject.twomillustratedbook.ui.screen.droplist.map.item

import com.jinproject.domain.model.MapField

data class MapState(
    val name: MapField,
    val imgName: String
) {
    companion object {
        fun getInitValue() = MapState(
            name = MapField.WOODY_WEEDY_FOREST,
            imgName = ""
        )
    }
}
