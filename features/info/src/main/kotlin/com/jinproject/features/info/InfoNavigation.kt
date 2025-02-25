package com.jinproject.features.info

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.Route
import com.jinproject.features.core.compose.TopLevelRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class InfoRoute : Route {
    @Serializable
    internal data object InfoGraph : InfoRoute()

    @Serializable
    data object Info : InfoRoute(), TopLevelRoute {
        override val icon: Int
            get() = com.jinproject.design_ui.R.drawable.ic_setting
        override val iconClicked: Int
            get() = com.jinproject.design_ui.R.drawable.ic_setting_clicked
    }

    @Serializable
    internal data object InfoChange : InfoRoute()

    @Serializable
    internal data object Term : InfoRoute()

    @Serializable
    internal data object LogOut : InfoRoute()

    @Serializable
    internal data object SignOut : InfoRoute()

}

fun NavGraphBuilder.infoNavigation(
    billingModule: BillingModule,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateInfoRoute: (InfoRoute) -> Unit,
    navigateToAuthGraph: () -> Unit,
    navigatePopBackStack: () -> Unit,
) {
    navigation<InfoRoute.InfoGraph>(startDestination = InfoRoute.Info) {
        composable<InfoRoute.InfoChange> {
            InfoChangeScreen(
                showSnackBar = showSnackBar,
                navigatePopBackStack = navigatePopBackStack,
            )
        }

        composable<InfoRoute.Info> {
            InfoScreen(
                billingModule = billingModule,
                showSnackBar = showSnackBar,
                navigateRoute = navigateInfoRoute,
                navigateToAuthGraph = navigateToAuthGraph,
            )
        }

        composable<InfoRoute.Term> {
            TermScreen(navigatePopBackStack = navigatePopBackStack)
        }
    }

}

fun NavController.navigateToInfoGraph(navOptions: NavOptions?) =
    navigate(InfoRoute.InfoGraph, navOptions)

fun NavController.navigateInfoRoute(infoRoute: InfoRoute) = navigate(infoRoute)