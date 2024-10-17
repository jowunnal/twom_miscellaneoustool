package com.jinproject.features.core.utils

import android.net.Uri

fun toAssetImageUri(prefix: String, imgName: String) = Uri.parse("file:///android_asset/img/$prefix/$imgName.png")