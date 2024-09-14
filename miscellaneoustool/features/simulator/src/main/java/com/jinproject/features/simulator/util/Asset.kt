package com.jinproject.features.simulator.util

import android.net.Uri

internal fun String.toImageUriOnAsset() = Uri.parse("file:///android_asset/img/item/$this.png")