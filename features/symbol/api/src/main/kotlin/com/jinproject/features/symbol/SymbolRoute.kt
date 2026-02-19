package com.jinproject.features.symbol

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface SymbolRoute : NavKey {
    @Serializable
    data object Symbol : SymbolRoute

    @Serializable
    data object Gallery : SymbolRoute

    @Serializable
    data class Detail(val imgUri: String) : SymbolRoute

    @Serializable
    data class GuildMark(val imgUri: String) : SymbolRoute

    @Serializable
    data class GuildMarkPreview(val imgUri: String) : SymbolRoute

    @Serializable
    data object GenerateImage : SymbolRoute

    @Serializable
    data object PurchasedImage : SymbolRoute
}
