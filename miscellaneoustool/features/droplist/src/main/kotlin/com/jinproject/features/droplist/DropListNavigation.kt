package com.jinproject.features.droplist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jinproject.features.core.Route
import kotlinx.serialization.Serializable

@Serializable
sealed class DropListRoute: Route {
    @Serializable
    data class MapList(val mapName: String?): DropListRoute()
}

fun NavGraphBuilder.dropListNavGraph() {
    composable<DropListRoute.MapList> {
        MapListScreen()
    }
}

fun NavController.navigateToDropList(mapName: String?) {
    navigate(DropListRoute.MapList(mapName))
}