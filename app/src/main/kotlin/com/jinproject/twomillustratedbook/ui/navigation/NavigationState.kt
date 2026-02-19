package com.jinproject.twomillustratedbook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.jinproject.features.core.compose.TopLevelNavItem

@Composable
internal fun rememberNavigationState(
    topLevelNavItems: Set<TopLevelNavItem>,
    startRoute: NavKey = topLevelNavItems.minBy { it.order }.route,
): NavigationState {
    val topLevelBackStack = rememberNavBackStack(startRoute)

    val tabBackStacks = topLevelNavItems.associate { item ->
        item.route to rememberNavBackStack(item.route)
    }

    return remember {
        NavigationState(
            topLevelNavItems = topLevelNavItems,
            topLevelBackStack = topLevelBackStack,
            leafBackStacks = tabBackStacks,
        )
    }
}

@Stable
internal class NavigationState(
    val topLevelNavItems: Set<TopLevelNavItem>,
    val topLevelBackStack: NavBackStack<NavKey>,
    val leafBackStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    private val topLevelRoutes: Set<NavKey> = topLevelNavItems.map { it.route }.toSet()
    private var isTopLevelRouteChanged = true

    val topLevelRoute: NavKey
        get() = topLevelBackStack.lastOrNull() ?: topLevelNavItems.minBy { it.order }.route

    val currentBackStack: NavBackStack<NavKey>
        get() = leafBackStacks[topLevelRoute] ?: error("No back stack for $topLevelRoute")

    val currentLeafRoute: NavKey
        get() = currentBackStack.lastOrNull() ?: topLevelRoute

    val isOnTopLevelDestination: Boolean
        get() = currentLeafRoute in topLevelRoutes

    fun updateIsTopLevelRouteChanged(bool: Boolean) {
        isTopLevelRouteChanged = bool
    }

    fun getTopLevelRouteSlideDirection(): Int {
        if (!isTopLevelRouteChanged)
            return 1

        val backStack = topLevelBackStack
        if (backStack.size < 2) return 1
        val currentRoute = backStack.getOrNull(backStack.lastIndex)
        val previousRoute = backStack.getOrNull(backStack.lastIndex - 1)
        val currentOrder =
            topLevelNavItems.firstOrNull { it.route == currentRoute }?.order ?: return 1
        val previousOrder =
            topLevelNavItems.firstOrNull { it.route == previousRoute }?.order ?: return 1
        return if (currentOrder > previousOrder) 1 else -1
    }
}
