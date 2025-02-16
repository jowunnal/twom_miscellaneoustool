package com.jinproject.features.symbol.purchasedList

import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiStatePreviewParameter
import com.jinproject.features.symbol.ImageListPreviewData
import com.jinproject.features.symbol.gallery.MTImage
import kotlinx.collections.immutable.PersistentList

internal class PurchasedListPreviewParameter :
    DownloadableUiStatePreviewParameter<PersistentList<MTImage>>() {
    override val data: DownLoadedUiState<PersistentList<MTImage>>
        get() = PurchasedListUiState(ImageListPreviewData.items)
}