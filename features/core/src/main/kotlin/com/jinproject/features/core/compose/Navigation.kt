package com.jinproject.features.core.compose

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey

@Immutable
data class TopLevelNavItem(
    val route: NavKey,
    @DrawableRes val icon: Int,
    @DrawableRes val iconClicked: Int,
    val order: Int,
)