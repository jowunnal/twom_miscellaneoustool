package com.jinproject.data.repository.model

import com.jinproject.domain.entity.TwomMap

data class MapModel(
    val name: String,
    val imgName: String
)

fun MapModel.toTwomMap() = TwomMap(
    name = name,
    imageName = imgName
)

fun List<MapModel>.toTwomMapList() = map { it.toTwomMap() }