package com.jinproject.core.util

import android.content.Intent
import android.os.Build
import android.os.Parcelable

inline fun <reified T: Parcelable> Intent.getParcelableExtraOnVersion(key: String) =
    if(Build.VERSION.SDK_INT >= 33) {
        getParcelableExtra(key, T::class.java)
    } else {
        getParcelableExtra(key)
    }