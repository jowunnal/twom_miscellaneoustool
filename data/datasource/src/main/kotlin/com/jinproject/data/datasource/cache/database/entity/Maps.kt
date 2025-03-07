package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jinproject.data.repository.model.MapModel

@Entity
data class Maps(
    @PrimaryKey val mapName: String,
    val mapImgName: String
)

fun Maps.toMapDataModel() = MapModel(
    name = mapName,
    imgName = mapImgName,
)

fun List<Maps>.toMapDataModelList() = map { it.toMapDataModel() }