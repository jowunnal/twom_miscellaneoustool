package com.jinproject.data.repository.model

data class MapModel(
    val name: String,
    val imgName: String
)

fun MapModel.toDomainModel() = com.jinproject.domain.model.MapModel(
    name = name,
    imgName = imgName
)

fun List<MapModel>.toDomainModel() = map { it.toDomainModel() }