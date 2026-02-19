package com.jinproject.features.droplist.state

import android.os.Parcelable
import com.jinproject.domain.entity.TwomMap
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapState(
    val name: String,
    val imgName: String
) : Parcelable {
    companion object {
        fun getInitValue() = MapState(
            name = "우디우디우디숲",
            imgName = "bulldozerbro"
        )
    }
}

fun TwomMap.toMapState(): MapState = MapState(
    name = name,
    imgName = imageName
)
