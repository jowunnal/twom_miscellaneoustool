package com.jinproject.features.droplist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.core.Route
import kotlinx.serialization.Serializable

@Serializable
sealed class DropListRoute: Route {

    @Serializable
    data object DropListGraph: DropListRoute()

    @Serializable
    data object MapList: DropListRoute()

}

fun NavGraphBuilder.dropListNavigation(
    setBottomBarVisibility: (Int) -> Unit,
) {
    navigation<DropListRoute.DropListGraph>(
        startDestination = DropListRoute.MapList,
    ) {
        composable<DropListRoute.MapList> {
            MapListScreen(
                setBottomBarVisibility = setBottomBarVisibility,
            )
        }
    }
}