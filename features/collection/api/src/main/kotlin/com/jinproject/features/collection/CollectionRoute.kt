package com.jinproject.features.collection

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class CollectionRoute : NavKey {
    @Serializable
    data object CollectionList : CollectionRoute()

    @Serializable
    data class CollectionDetail(val id: Int) : CollectionRoute()
}
