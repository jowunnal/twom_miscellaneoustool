package com.jinproject.features.symbol

import android.view.View
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.detail.DetailScreen
import com.jinproject.features.symbol.gallery.GalleryScreen
import com.jinproject.features.symbol.guildmark.GuildMarkScreen
import com.jinproject.features.symbol.preview.PreviewScreen
import com.jinproject.features.symbol.symbol.SymbolScreen

const val SymbolGraph = "symbolGraph"
const val SymbolRoute = "symbol"
const val GalleryRoute = "gallery"
const val DetailImageUri = "imageDetailUri"
const val DetailRoute = "detail"
const val DetailLink = "$DetailRoute/{$DetailImageUri}"
const val GuildMarkRoute = "guildMark"
const val GuildMarkLink = "$GuildMarkRoute/{$DetailImageUri}"
const val GuildMarkPreviewRoute = "guildMarkPreview"
const val GuildMarkPreviewLink = "$GuildMarkPreviewRoute/{$DetailImageUri}"

fun NavGraphBuilder.symbolNavGraph(
    modifier: Modifier = Modifier,
    navController: NavController,
    billingModule: BillingModule,
    showSnackBar: (SnackBarMessage) -> Unit,
    setBottomBarVisibility: (Int) -> Unit,
) {
    navigation(
        startDestination = SymbolRoute,
        route = SymbolGraph,
    ) {
        composable(
            route = SymbolRoute,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            },
        ) {
            setBottomBarVisibility(View.VISIBLE)
            SymbolScreen(
                modifier = modifier,
                navigateToGallery = {
                    navController::navigateToGallery.invoke()
                },
                showSnackBar = showSnackBar,
            )
        }
        composable(
            route = GalleryRoute,
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
            setBottomBarVisibility(View.GONE)
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

        composable(
            route = DetailLink,
            arguments = listOf(navArgument(DetailImageUri) {
                type = NavType.StringType
            }),
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

        composable(
            route = GuildMarkLink,
            arguments = listOf(navArgument(DetailImageUri) {
                type = NavType.StringType
            }),
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

        composable(
            route = GuildMarkPreviewLink,
            arguments = listOf(navArgument(DetailImageUri) {
                type = NavType.StringType
            }),
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

internal fun String.toParsedString() = this.replace("/","*")

internal fun String.toParsedUri() = this.replace("*","/").toUri()

fun NavController.navigateToGuildMark(imageUri: String) {
    this.navigate("$GuildMarkRoute/$imageUri", navOptions { popUpTo(GalleryRoute) })
}

fun NavController.navigateToGallery() {
    this.navigate(GalleryRoute)
}

fun NavController.navigateToDetail(imageUri: String) {
    this.navigate("$DetailRoute/$imageUri")
}

fun NavController.navigateToGuildMarkPreview(imageUri: String) {
    this.navigate("$GuildMarkPreviewRoute/$imageUri")
}

fun NavController.popBackStackIfCan() {
    this.previousBackStackEntry?.let {
        this.popBackStack()
    }
}