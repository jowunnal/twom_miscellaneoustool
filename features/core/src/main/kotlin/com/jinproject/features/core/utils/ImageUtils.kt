package com.jinproject.features.core.utils

import android.net.Uri
import androidx.core.net.toUri

fun toAssetImageUri(prefix: String, imgName: String): Uri =
    "file:///android_asset/${getImageUri(prefix = prefix, imgName = imgName)}".toUri()

fun getImageUri(prefix: String, imgName: String) = "img/$prefix/$imgName.webp"