package com.jinproject.features.symbol

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.Route
import com.jinproject.features.core.compose.TopLevelRoute
import com.jinproject.features.symbol.ai.GenerateImageScreen
import com.jinproject.features.symbol.detail.DetailScreen
import com.jinproject.features.symbol.gallery.GalleryScreen
import com.jinproject.features.symbol.guildmark.GuildMarkScreen
import com.jinproject.features.symbol.preview.PreviewScreen
import com.jinproject.features.symbol.purchasedList.PurchasedListScreen
import com.jinproject.features.symbol.symbol.SymbolScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class SymbolRoute : Route {
    @Serializable
    data object SymbolGraph : SymbolRoute()

    @Serializable
    data object Symbol : SymbolRoute(), TopLevelRoute {
        override val icon: Int = com.jinproject.design_ui.R.drawable.ic_guild_symbol
        override val iconClicked: Int = com.jinproject.design_ui.R.drawable.ic_guild_symbol
    }

    @Serializable
    data object Gallery : SymbolRoute()

    @Serializable
    data class Detail(val imgUri: String) : SymbolRoute()

    @Serializable
    data class GuildMark(val imgUri: String) : SymbolRoute()

    @Serializable
    data class GuildMarkPreview(val imgUri: String) : SymbolRoute()

    @Serializable
    data object GenerateImage : SymbolRoute()

    @Serializable
    data object PurchasedImage : SymbolRoute()
}

fun NavGraphBuilder.symbolNavGraph(
    modifier: Modifier = Modifier,
    navController: NavController,
    billingModule: BillingModule,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToAuthGraph: () -> Unit,
) {
    navigation<SymbolRoute.SymbolGraph>(
        startDestination = SymbolRoute.Symbol,
    ) {
        composable<SymbolRoute.Symbol> {
            SymbolScreen(
                modifier = modifier,
                navigateToGallery = navController::navigateToGallery,
                showSnackBar = showSnackBar,
                navigateToGenerateImage = navController::navigateToGenerateImage,
                navigateToAuthGraph = navigateToAuthGraph,
            )
        }

        composable<SymbolRoute.Gallery>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
        ) {
            GalleryScreen(
                navigateToImageDetail = navController::navigateToDetail,
                popBackStack = navController::popBackStackIfCan,
                showSnackBar = showSnackBar,
                navigateToGuildMarkPreview = navController::navigateToGuildMarkPreview,
                navigateToGuildMark = { uri ->
                    navController.navigateToGuildMark(uri.toParsedString())
                },
                navigateToPurchasedImage = navController::navigateToPurchasedImage,
                navigateToAuthGraph = navigateToAuthGraph,
            )
        }

        composable<SymbolRoute.Detail>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
        ) {
            DetailScreen(
                popBackStack = navController::popBackStackIfCan,
                showSnackBar = showSnackBar,
            )
        }

        composable<SymbolRoute.GuildMark>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
        ) {
            GuildMarkScreen(
                popBackStack = navController::popBackStackIfCan,
                showSnackBar = showSnackBar,
            )
        }

        composable<SymbolRoute.GuildMarkPreview>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
        ) {
            PreviewScreen(
                billingModule = billingModule,
                popBackStack = navController::popBackStackIfCan,
                showSnackBar = showSnackBar,
                navigateToGuildMark = { uri ->
                    navController.navigateToGuildMark(uri.toParsedString())
                }
            )
        }

        composable<SymbolRoute.GenerateImage>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
        ) {
            GenerateImageScreen(
                billingModule = billingModule,
                navigateToBack = navController::popBackStackIfCan,
                navigateToImageDetail = navController::navigateToDetail,
                navigateToGuildMarkPreview = navController::navigateToGuildMarkPreview,
                navigateToPurchasedImage = navController::navigateToPurchasedImage,
                navigateToAuthGraph = navigateToAuthGraph,
                showSnackBar = showSnackBar,
            )
        }

        composable<SymbolRoute.PurchasedImage> {
            PurchasedListScreen(
                navigateToItemDetail = navController::navigateToDetail,
                navigateToGuildMark = { uri ->
                    navController.navigateToGuildMark(uri.toParsedString())
                },
                navigatePopBackStack = navController::popBackStackIfCan,
            )
        }

    }
}

internal fun String.toParsedString() = this.replace("/", "*")

internal fun String.toParsedUri() = this.replace("*", "/")

fun NavController.navigateToSymbolGraph(navOptions: NavOptions? = null) {
    navigate(SymbolRoute.SymbolGraph, navOptions)
}

fun NavController.navigateToGuildMark(imageUri: String) {
    this.navigate(SymbolRoute.GuildMark(imageUri), navOptions { popUpTo(SymbolRoute.Gallery) })
}

fun NavController.navigateToGallery() {
    this.navigate(SymbolRoute.Gallery)
}

fun NavController.navigateToDetail(imageUri: String) {
    this.navigate(SymbolRoute.Detail(imageUri.toParsedString()))
}

fun NavController.navigateToGuildMarkPreview(imageUri: String) {
    this.navigate(SymbolRoute.GuildMarkPreview(imageUri.toParsedString()))
}

fun NavController.navigateToGenerateImage(navOptions: NavOptions? = null) {
    navigate(SymbolRoute.GenerateImage, navOptions)
}

fun NavController.navigateToPurchasedImage() {
    navigate(SymbolRoute.PurchasedImage)
}

fun NavController.popBackStackIfCan() {
    this.previousBackStackEntry?.let {
        this.popBackStack()
    }
}