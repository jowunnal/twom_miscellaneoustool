package com.jinproject.features.symbol.gallery


import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jinproject.features.symbol.ImageListPreviewData

internal class GalleryPreviewParameters: PreviewParameterProvider<MTImageList> {
    override val values: Sequence<MTImageList> = sequenceOf(
        MTImageList(
            images = ImageListPreviewData.items
        )
    )
}