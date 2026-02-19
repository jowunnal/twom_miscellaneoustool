package com.jinproject.features.droplist

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class DropListRoute : NavKey {
    @Serializable
    data object MapList : DropListRoute()

    @Serializable
    data class MapDetail(val mapName: String) : DropListRoute()
}
