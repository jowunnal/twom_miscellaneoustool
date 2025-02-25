package com.jinproject.features.core.compose

import androidx.annotation.DrawableRes

interface Route

interface TopLevelRoute {
    @get:DrawableRes
    val icon: Int

    @get:DrawableRes
    val iconClicked: Int
}