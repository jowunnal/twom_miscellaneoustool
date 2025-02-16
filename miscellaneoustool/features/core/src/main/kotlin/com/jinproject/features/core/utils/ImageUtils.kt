package com.jinproject.features.core.utils

import android.net.Uri

fun toAssetImageUri(prefix: String, imgName: String): Uri = Uri.parse("file:///android_asset/${getImageUri(prefix = prefix, imgName = imgName)}")

fun getImageUri(prefix: String, imgName: String) = "img/$prefix/$imgName.webp"