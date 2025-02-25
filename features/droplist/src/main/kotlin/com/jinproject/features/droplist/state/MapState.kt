package com.jinproject.features.droplist.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapState(
    val name: String,
    val imgName: String
) : Parcelable {
    companion object {
        fun getInitValue() = MapState(
            name = "우디위디숲",
            imgName = "bulldozerbro"
        )
    }
}
