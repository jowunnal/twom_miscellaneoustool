package com.jinproject.twomillustratedbook.ui.navigation


import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import com.jinproject.features.alarm.alarmNavGraph
import com.jinproject.features.alarm.navigateToAlarmGraph
import com.jinproject.features.alarm.navigateToAlarmSetting
import com.jinproject.features.auth.authNavigation
import com.jinproject.features.auth.navigateAuthRoute
import com.jinproject.features.auth.navigateToAuthGraph
import com.jinproject.features.collection.navigateToCollectionList
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.TopLevelRoute
import com.jinproject.features.droplist.navigateToDropList
import com.jinproject.features.home.HomeRoute
import com.jinproject.features.home.homeNavGraph
import com.jinproject.features.info.infoNavigation
import com.jinproject.features.info.navigateInfoRoute
import com.jinproject.features.simulator.simulatorNavGraph
import com.jinproject.features.symbol.SymbolRoute
import com.jinproject.features.symbol.navigateToGenerateImage
import com.jinproject.features.symbol.popBackStackIfCan
import com.jinproject.features.symbol.symbolNavGraph

@Composable
internal fun NavigationGraph(
    modifier: Modifier = Modifier,
    router: Router,
    billingModule: BillingModule,
    showRewardedAd: (() -> Unit) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val navController = router.navController

    NavHost(
        navController = navController,
        startDestination = HomeRoute.HomeGraph,
        modifier = modifier
    ) {

        homeNavGraph(
            navigateToDropList = navController::navigateToDropList,
            navigateToCollection = navController::navigateToCollectionList,
            navigateToAlarm = { navController.navigateToAlarmGraph(null) },
            showSnackBar = showSnackBar,
            popBackStackIfCan = navController::popBackStackIfCan,
        )

        alarmNavGraph(
            billingModule = billingModule,
            showRewardedAd = showRewardedAd,
            onNavigateToAlarmSetting = navController::navigateToAlarmSetting,
            showSnackBar = showSnackBar,
            popBackStackIfCan = navController::popBackStackIfCan,
        )

        symbolNavGraph(
            billingModule = billingModule,
            navController = navController,
            showSnackBar = showSnackBar,
            navigateToAuthGraph = navController::navigateToAuthGraph,
        )

        simulatorNavGraph()

        authNavigation(
            navigatePopBackStack = navController::popBackStackIfCan,
            navigateAuthRoute = navController::navigateAuthRoute,
            showSnackBar = showSnackBar,
            navigatePopBackStackToRoute = navController::popBackStackIfCan,
            navigateToGenerateImage = navController::navigateToGenerateImage,
            isPreviousDestinationGenerateImage = {
                navController.previousBackStackEntry?.destination?.hasRoute(SymbolRoute.GenerateImage::class) == true
            }
        )

        infoNavigation(
            billingModule = billingModule,
            showSnackBar = showSnackBar,
            navigateToAuthGraph = navController::navigateToAuthGraph,
            navigateInfoRoute = navController::navigateInfoRoute,
            navigatePopBackStack = navController::popBackStackIfCan
        )
    }
}

internal fun NavigationSuiteScope.navigationSuiteItems(
    currentDestination: NavDestination?,
    itemColors: NavigationSuiteItemColors,
    onClick: (TopLevelRoute) -> Unit,
) {
    TopLevelRoutes.forEach { destination ->
        val selected = currentDestination.isDestinationInHierarchy(destination)

        item(
            selected = selected,
            onClick = { onClick(destination) },
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