package com.jinproject.features.collection

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.Equipment
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.collection.model.MiscellaneousItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

class CollectionUiStatePreviewParameter : PreviewParameterProvider<CollectionUiState> {
    override val values: Sequence<CollectionUiState>
        get() = sequenceOf(
            CollectionUiState(
                itemCollections = items,
                collectionFilters = filters,
                selectedCollectionId = null,
            )
        )

    companion object {
        val items = persistentListOf(
            ItemCollection(
                id = 0,
                stats = persistentMapOf(
                    "pve데미지증가%" to 0.5f,
                    "pvp데미지증가" to 0.5f,
                    "pve데미지감소" to 0.5f,
                ),
                items = persistentListOf(
                    MiscellaneousItem(
                        name = "고대코어",
                        count = 180,
                        price = 400000000,
                        imageName = "ancient_core",
                    ),
                    MiscellaneousItem(
                        name = "고대의 봉",
                        count = 25,
                        price = 1000,
                        imageName = "ancient_wand",
                    )
                ),
            ),
            ItemCollection(
                id = 1,
                stats = persistentMapOf(
                    "체력" to 3.0f,
                    "마나" to 1.0f,
                ),
                items = persistentListOf(
                    MiscellaneousItem(
                        name = "고대코어",
                        count = 300,
                        price = 100,
                        imageName = "ancient_core",
                    ),
                ),
            ),
            ItemCollection(
                id = 2,
                stats = persistentMapOf(
                    "pve데미지증가%" to 0.5f,
                    "pvp데미지증가" to 0.5f,
                    "pve데미지감소" to 0.5f,
                ),
                items = persistentListOf(
                    MiscellaneousItem(
                        name = "고대코어",
                        count = 900,
                        price = 100,
                        imageName = "ancient_core",
                    ),
                ),
            ),
            ItemCollection(
                id = 3,
                stats = persistentMapOf(
                    "pve데미지증가%" to 0.5f,
                    "pvp데미지증가" to 0.5f,
                ),
                items = persistentListOf(
                    MiscellaneousItem(
                        name = "고대코어",
                        count = 180,
                        price = 400000000,
                        imageName = "ancient_core",
                    ),
                    Equipment(
                        name = "고대의 봉",
                        count = 1,
                        enchantNumber = 5,
                        price = 10000,
                        imageName = "ancient_wand",
                    )
                ),
            ),
        )

        val filters = persistentSetOf<Int>(0)
    }
}