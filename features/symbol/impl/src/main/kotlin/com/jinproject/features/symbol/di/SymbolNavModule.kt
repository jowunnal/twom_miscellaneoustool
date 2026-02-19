package com.jinproject.features.symbol.di

import com.jinproject.features.core.compose.TopLevelNavItem
import com.jinproject.features.symbol.SymbolRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object SymbolNavModule {
    @Provides
    @IntoSet
    fun provideTopLevelNavItem(): TopLevelNavItem = TopLevelNavItem(
        route = SymbolRoute.Symbol,
        icon = com.jinproject.design_ui.R.drawable.ic_guild_symbol,
        iconClicked = com.jinproject.design_ui.R.drawable.ic_guild_symbol,
        order = 2,
    )
}
