package com.jinproject.features.collection.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.jinproject.core.util.runIf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList

data class CollectionUiState(
    val itemCollections: ImmutableList<ItemCollection>,
    val collectionFilters: ImmutableSet<Int>,
    val selectedCollectionId: Int?,
) {
    /**
     * 아이템 도감 목록에 필터링 목록으로 필터링 한 결과를 반환하는 함수
     *
     * @param isFiltering 필터링 유무
     * @return 필터링된 아이템 도감 목록
     */
    private fun filter(
        isFiltering: Boolean,
        filterBuffer: SnapshotStateList<Int>,
    ): ImmutableList<ItemCollection> {
        return if (!isFiltering)
            itemCollections
        else
            itemCollections.filter { itemCollection ->
                itemCollection.id !in filterBuffer
            }.toImmutableList()
    }

    /**
     * 필터링 목록 과 검색어로 필터링한 아이템 도감 목록을 반환하는 함수
     *
     * @param searchWord 검색어
     * @param isFiltering 필터링 유무
     * @return 필터링된 아이템 도감 목록
     */
    fun filterBySearchWord(
        searchWord: String,
        isFiltering: Boolean,
        filterBuffer: SnapshotStateList<Int>,
    ): ImmutableList<ItemCollection> {
        return filter(
            isFiltering = isFiltering,
            filterBuffer = filterBuffer,
        ).runIf(searchWord.isNotBlank()) {
            filter { collection ->
                collection.items.firstNotNullOfOrNull { item ->
                    item.name.contains(searchWord, true)
                } == true ||
                        collection.stats.firstNotNullOfOrNull { stat ->
                            stat.key.contains(searchWord, true)
                        } == true
            }.toImmutableList()
        }
    }
}

data class CollectionFilter(
    val id: Int,
    val isSelected: Boolean = false,
)