package com.jinproject.twomillustratedbook.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.jinproject.features.alarm.alarmEntries
import com.jinproject.features.auth.authEntries
import com.jinproject.features.collection.collectionEntries
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.compose.TopLevelNavItem
import com.jinproject.features.droplist.dropListEntries
import com.jinproject.features.home.homeEntries
import com.jinproject.features.info.infoEntries
import com.jinproject.features.simulator.simulatorEntries
import com.jinproject.features.symbol.symbolEntries

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun NavigationGraph(
    modifier: Modifier = Modifier,
    navigator: AppNavigator,
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
) {
    NavDisplay(
        backStack = navigator.state.currentBackStack,
        modifier = modifier,
        onBack = { navigator.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        sceneStrategy = rememberListDetailSceneStrategy<NavKey>(),
        entryProvider = entryProvider {
            homeEntries()

            alarmEntries(
                billingModule = billingModule,
                showRewardedAd = showRewardedAd,
            )

            symbolEntries(
                billingModule = billingModule,
            )

            simulatorEntries()

            collectionEntries()

            dropListEntries()

            authEntries()

            infoEntries(
                billingModule = billingModule,
            )
        },
        transitionSpec = {
            val direction = navigator.state.getTopLevelRouteSlideDirection()

            (slideInHorizontally(initialOffsetX = { direction * it }) +
                    scaleIn(initialScale = 0.85f) + fadeIn())
                .togetherWith(
                    slideOutHorizontally(targetOffsetX = { -direction * it / 3 }) +
                            scaleOut(targetScale = 0.95f) + fadeOut()
                )
        },
        popTransitionSpec = {
            val direction = -navigator.state.getTopLevelRouteSlideDirection()

            (slideInHorizontally(initialOffsetX = { direction * it / 3 }) +
                    scaleIn(initialScale = 0.95f) + fadeIn())
                .togetherWith(
                    slideOutHorizontally(targetOffsetX = { -direction * it }) +
                            scaleOut(targetScale = 0.85f) + fadeOut()
                )
        },
        predictivePopTransitionSpec = {
            val direction = -navigator.state.getTopLevelRouteSlideDirection()

            (slideInHorizontally(initialOffsetX = { direction * it / 3 }) +
                    scaleIn(initialScale = 0.95f) + fadeIn())
                .togetherWith(
                    slideOutHorizontally(targetOffsetX = { -direction * it }) +
                            scaleOut(targetScale = 0.85f) + fadeOut()
                )
        },
    )
}

internal fun NavigationSuiteScope.navigationSuiteItems(
    topLevelNavItems: Set<TopLevelNavItem>,
    currentRoute: NavKey,
    itemColors: NavigationSuiteItemColors,
    onClick: (NavKey) -> Unit,
) {
    topLevelNavItems.sortedBy { it.order }.forEach { destination ->
        val selected = currentRoute == destination.route

        item(
            selected = selected,
            onClick = { onClick(destination.route) },
            icon = {
                if (!selected)
                    Icon(
                        imageVector = ImageVector.vectorResource(id = destination.icon),
                        contentDescription = "clickIcon",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                else
                    Icon(
                        imageVector = ImageVector.vectorResource(id = destination.iconClicked),
                        contentDescription = "clickedIcon",
                        tint = MaterialTheme.colorScheme.primary,
                    )
            },
            colors = itemColors,
        )
    }
}

@Immutable
internal object NavigationDefaults {
    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.surface

    @Composable
    fun containerColor() = MaterialTheme.colorScheme.surface

    @Composable
    fun contentColor() = MaterialTheme.colorScheme.onSurface
}
