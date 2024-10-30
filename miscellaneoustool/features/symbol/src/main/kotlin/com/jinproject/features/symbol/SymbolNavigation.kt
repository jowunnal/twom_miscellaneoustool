package com.jinproject.features.symbol

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.Route
import com.jinproject.features.core.TopLevelRoute
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.detail.DetailScreen
import com.jinproject.features.symbol.gallery.GalleryScreen
import com.jinproject.features.symbol.guildmark.GuildMarkScreen
import com.jinproject.features.symbol.preview.PreviewScreen
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
}

fun NavGraphBuilder.symbolNavGraph(
    modifier: Modifier = Modifier,
    navController: NavController,
    billingModule: BillingModule,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    navigation<SymbolRoute.SymbolGraph>(
        startDestination = SymbolRoute.Symbol,
    ) {
        composable<SymbolRoute.Symbol> {
            SymbolScreen(
                modifier = modifier,
                navigateToGallery = {
                    navController::navigateToGallery.invoke()
                },
                showSnackBar = showSnackBar,
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
                navigateToImageDetail = { uri ->
                    navController::navigateToDetail.invoke(uri.toParsedString())
                },
                popBackStack = {
                    navController::popBackStackIfCan.invoke()
                },
                showSnackBar = showSnackBar,
                navigateToGuildMarkPreview = { uri ->
                    navController::navigateToGuildMarkPreview.invoke(uri.toParsedString())
                },
                navigateToGuildMark = { uri ->
                    navController::navigateToGuildMark.invoke(uri.toParsedString())
                }
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
                    navController::navigateToGuildMark.invoke(uri.toParsedString())
                }
            )
        }
    }
}

internal fun String.toParsedString() = this.replace("/", "*")

internal fun String.toParsedUri() = this.replace("*", "/").toUri()

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
    this.navigate(SymbolRoute.Detail(imageUri))
}

fun NavController.navigateToGuildMarkPreview(imageUri: String) {
    this.navigate(SymbolRoute.GuildMarkPreview(imageUri))
}

fun NavController.popBackStackIfCan() {
    this.previousBackStackEntry?.let {
        this.popBackStack()
    }
}