package com.jinproject.features.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.features.collection.CollectionUiStatePreviewParameter
import com.jinproject.features.droplist.DropListUiStatePreviewParameter

class HomePreviewParameter: PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState>
        get() = sequenceOf(
            HomeUiState(
                maps = DropListUiStatePreviewParameter.maps,
                collections = CollectionUiStatePreviewParameter.items,
            )
        )
}