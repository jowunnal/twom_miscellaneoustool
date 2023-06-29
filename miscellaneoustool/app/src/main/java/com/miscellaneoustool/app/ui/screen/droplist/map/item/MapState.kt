package com.miscellaneoustool.app.ui.screen.droplist.map.item

data class MapState(
    val name: String,
    val imgName: String
) {
    companion object {
        fun getInitValue() = MapState(
            name = "",
            imgName = ""
        )
    }
}
