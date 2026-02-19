package com.jinproject.twomillustratedbook.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.jinproject.features.core.compose.Navigator

internal class AppNavigator(val state: NavigationState) : Navigator {

    private val topLevelRoutes: Set<NavKey> = state.topLevelNavItems.map { it.route }.toSet()

    override val currentBackStack: List<NavKey>
        get() = state.currentBackStack.toList()

    override fun navigate(route: NavKey) {
        if (route in topLevelRoutes) {
            switchTab(route)
            state.updateIsTopLevelRouteChanged(true)
        } else {
            state.currentBackStack.add(route)
            state.updateIsTopLevelRouteChanged(false)
        }
    }

    override fun goBack() {
        val currentBackStack = state.currentBackStack
        if (currentBackStack.size > 1) {
            currentBackStack.removeLastOrNull()
        } else {
            state.topLevelBackStack.removeLastOrNull()
        }
    }

    override fun popTo(route: NavKey, inclusive: Boolean) {
        val backStack = state.currentBackStack
        val index = backStack.indexOfLast { it == route }
        if (index >= 0) {
            val removeFrom = if (inclusive) index else index + 1
            while (backStack.size > removeFrom) {
                backStack.removeLastOrNull()
            }
        }
    }

    override fun replaceAbove(anchorRoute: NavKey, newRoute: NavKey) {
        popTo(anchorRoute, inclusive = false)
        state.currentBackStack.add(newRoute)
    }

    private fun switchTab(route: NavKey) {
        val currentTop = state.topLevelBackStack.lastOrNull()
        if (currentTop == route) return

        val existingIndex = state.topLevelBackStack.indexOf(route)
        if (existingIndex >= 0) {
            state.topLevelBackStack.removeAt(existingIndex)
        }
        state.topLevelBackStack.add(route)
    }
}
