package com.jinproject.features.home.di

import com.jinproject.features.core.compose.TopLevelNavItem
import com.jinproject.features.home.HomeRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object HomeNavModule {
    @Provides
    @IntoSet
    fun provideTopLevelNavItem(): TopLevelNavItem = TopLevelNavItem(
        route = HomeRoute.Home,
        icon = com.jinproject.design_ui.R.drawable.icon_home,
        iconClicked = com.jinproject.design_ui.R.drawable.icon_home,
        order = 0,
    )
}
