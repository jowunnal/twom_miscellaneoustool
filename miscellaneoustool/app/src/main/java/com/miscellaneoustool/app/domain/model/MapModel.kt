package com.miscellaneoustool.app.domain.model

import com.miscellaneoustool.app.data.database.entity.Map
import com.miscellaneoustool.app.ui.screen.droplist.map.item.MapState

data class MapModel(
    val name: String,
    val imgName: String
) {

    fun toMapState() = MapState(
        name = name,
        imgName = imgName
    )

    companion object {
        fun fromResponse(map: Map) = MapModel(
            name = map.mapName,
            imgName = map.mapImgName
        )
    }
}
