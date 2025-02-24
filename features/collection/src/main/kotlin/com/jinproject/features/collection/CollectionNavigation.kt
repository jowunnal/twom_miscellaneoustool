package com.jinproject.features.collection

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jinproject.features.core.compose.Route
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.serialization.Serializable

@Serializable
sealed class CollectionRoute : Route {
    @Serializable
    data object CollectionGraph : CollectionRoute()

    @Serializable
    data class CollectionList(val id: Int?) : CollectionRoute()
}

fun NavGraphBuilder.collectionNavGraph(
    showSnackBar: (SnackBarMessage) -> Unit,
    popBackStackIfCan: () -> Unit,
) {
    composable<CollectionRoute.CollectionList> {
        CollectionScreen(
            showSnackBar = showSnackBar,
            onNavigateBack = popBackStackIfCan,
        )
    }
}

fun NavController.navigateToCollectionList(id: Int?) {
    navigate(CollectionRoute.CollectionList(id))
}