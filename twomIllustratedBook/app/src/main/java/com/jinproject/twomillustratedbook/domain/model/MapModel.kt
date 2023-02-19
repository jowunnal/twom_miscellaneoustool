package com.jinproject.twomillustratedbook.domain.model

import com.jinproject.twomillustratedbook.data.database.Entity.Map
import com.jinproject.twomillustratedbook.ui.screen.droplist.map.item.MapState

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
