package com.jinproject.features.symbol

import com.jinproject.features.symbol.gallery.MTImage

object ImageListPreviewData {
    val items: List<MTImage> = mutableListOf<MTImage>().apply {
        repeat(100) { r ->
            add(
                MTImage(
                    id = r.toLong(),
                    uri = "",
                    modifiedDate = "",
                )
            )
        }
    }
}