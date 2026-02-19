package com.jinproject.features.core.utils

import android.net.Uri
import androidx.core.net.toUri

object AssetConfig {
    const val ITEM_PATH_PREFIX = "item"
    const val MONSTER_PATH_PREFIX = "monster"
}

fun toAssetImageUri(prefix: String, imgName: String): Uri =
    "file:///android_asset/${getImageUri(prefix = prefix, imgName = imgName)}".toUri()

fun getImageUri(prefix: String, imgName: String) = "img/$prefix/$imgName.webp"
