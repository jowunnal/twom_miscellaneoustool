package com.jinproject.features.core.utils

import android.content.Context
import coil.request.ImageRequest

fun getImageDataFromAsset(context: Context, prefix: String, imageName: String): ImageRequest {
    return ImageRequest
        .Builder(context)
        .data(
            toAssetImageUri(
                prefix = prefix,
                imgName = imageName
            )
        )
        .build()
}