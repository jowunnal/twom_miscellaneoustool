package com.jinproject.features.collection

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.core.Route
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.serialization.Serializable

@Serializable
sealed class CollectionRoute: Route {
    @Serializable
    data object CollectionGraph: CollectionRoute()
    @Serializable
    data object CollectionList: CollectionRoute()
}

fun NavGraphBuilder.collectionNavigation(
    showSnackBar: (SnackBarMessage) -> Unit,
    popBackStackIfCan: () -> Unit,
) {
    navigation<CollectionRoute.CollectionGraph>(
        startDestination = CollectionRoute.CollectionList,
    ) {
        composable<CollectionRoute.CollectionList> {
            CollectionScreen(
                showSnackBar = showSnackBar,
                onNavigateBack = popBackStackIfCan,
            )
        }
    }
}