package com.jinproject.features.symbol.gallery


import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiStatePreviewParameter
import com.jinproject.features.symbol.ImageListPreviewData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class GalleryPreviewParameters :
    DownloadableUiStatePreviewParameter<ImmutableList<MTImage>>() {
    override val data: DownLoadedUiState<ImmutableList<MTImage>>
        get() = GalleryUiState(
            data = ImageListPreviewData.items,
            paidImageUris = persistentListOf(),
        )
}