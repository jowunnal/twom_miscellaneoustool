package com.jinproject.data.repository.model

import com.jinproject.domain.entity.TwomMap

data class MapModel(
    val name: String,
    val imgName: String
)

fun MapModel.toDomainModel() = TwomMap(
    name = name,
    imageName = imgName
)

fun List<MapModel>.toDomainModelList() = map { it.toDomainModel() }