package com.jinproject.features.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.collection.collectionNavGraph
import com.jinproject.features.core.compose.Route
import com.jinproject.features.core.compose.TopLevelRoute
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.droplist.dropListNavGraph
import kotlinx.serialization.Serializable

@Serializable
sealed class HomeRoute: Route {
    @Serializable
    data object HomeGraph: HomeRoute()

    @Serializable
    data object Home : HomeRoute(), TopLevelRoute {
        override val icon: Int = com.jinproject.design_ui.R.drawable.icon_home
        override val iconClicked: Int = com.jinproject.design_ui.R.drawable.icon_home
    }
}

fun NavGraphBuilder.homeNavGraph(
    navigateToDropList: (String) -> Unit,
    navigateToCollection: (Int?) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    popBackStackIfCan: () -> Unit,
) {
    navigation<HomeRoute.HomeGraph>(
        startDestination = HomeRoute.Home,
    ) {
        composable<HomeRoute.Home> {
            HomeScreen(
                navigateToDropList = navigateToDropList,
                navigateToCollection = navigateToCollection,
            )
        }

        dropListNavGraph()

        collectionNavGraph(
            showSnackBar = showSnackBar,
            popBackStackIfCan = popBackStackIfCan,
        )
    }
}

fun NavController.navigateToHomeGraph(navOptions: NavOptions?) {
    navigate(HomeRoute.HomeGraph, navOptions)
}