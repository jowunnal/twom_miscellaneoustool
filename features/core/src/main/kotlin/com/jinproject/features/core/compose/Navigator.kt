package com.jinproject.features.core.compose

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey

@Stable
interface Navigator {
    val currentBackStack: List<NavKey>

    fun navigate(route: NavKey)
    fun goBack()
    fun popTo(route: NavKey, inclusive: Boolean = false)
    fun replaceAbove(anchorRoute: NavKey, newRoute: NavKey)
}
