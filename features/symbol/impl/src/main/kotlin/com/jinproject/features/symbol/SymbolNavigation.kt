package com.jinproject.features.symbol

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.jinproject.features.core.BillingModule
import com.jinproject.features.symbol.ai.GenerateImageScreen
import com.jinproject.features.symbol.detail.DetailScreen
import com.jinproject.features.symbol.gallery.GalleryScreen
import com.jinproject.features.symbol.guildmark.GuildMarkScreen
import com.jinproject.features.symbol.preview.PreviewScreen
import com.jinproject.features.symbol.purchasedList.PurchasedListScreen
import com.jinproject.features.symbol.symbol.SymbolScreen

private val slideLeftTransition = NavDisplay.transitionSpec {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
}

private val slideRightPopTransition = NavDisplay.popTransitionSpec {
    EnterTransition.None togetherWith slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
    )
}

private val slideLeftExitForwardTransition = NavDisplay.transitionSpec {
    EnterTransition.None togetherWith slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
    )
}

private val slideRightPopEnterTransition = NavDisplay.popTransitionSpec {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
}

fun EntryProviderScope<NavKey>.symbolEntries(
    billingModule: BillingModule,
) {
    entry<SymbolRoute.Symbol> {
        SymbolScreen()
    }

    entry<SymbolRoute.Gallery>(
        metadata = slideLeftTransition + slideLeftExitForwardTransition + slideRightPopEnterTransition + slideRightPopTransition
    ) {
        GalleryScreen()
    }

    entry<SymbolRoute.Detail>(
        metadata = slideLeftTransition + slideRightPopTransition
    ) { key ->
        DetailScreen(imgUri = key.imgUri)
    }

    entry<SymbolRoute.GuildMark>(
        metadata = slideLeftTransition + slideRightPopTransition
    ) { key ->
        GuildMarkScreen(imgUri = key.imgUri)
    }

    entry<SymbolRoute.GuildMarkPreview>(
        metadata = slideLeftTransition + slideLeftExitForwardTransition + slideRightPopEnterTransition + slideRightPopTransition
    ) { key ->
        PreviewScreen(
            imgUri = key.imgUri,
            billingModule = billingModule,
        )
    }

    entry<SymbolRoute.GenerateImage>(
        metadata = slideLeftTransition + slideLeftExitForwardTransition + slideRightPopEnterTransition + slideRightPopTransition
    ) {
        GenerateImageScreen(billingModule = billingModule)
    }

    entry<SymbolRoute.PurchasedImage> {
        PurchasedListScreen()
    }
}
