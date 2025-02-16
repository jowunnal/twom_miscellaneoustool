package com.jinproject.features.symbol

import com.jinproject.features.symbol.gallery.MTImage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

object ImageListPreviewData {
    val items: PersistentList<MTImage> = mutableListOf<MTImage>().apply {
        repeat(100) { r ->
            add(
                MTImage(
                    id = r.toLong(),
                    uri = "",
                    modifiedDate = "",
                )
            )
        }
    }.toPersistentList()
}