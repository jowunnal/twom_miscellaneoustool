package com.jinproject.features.info.di

import com.jinproject.features.core.compose.TopLevelNavItem
import com.jinproject.features.info.InfoRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object InfoNavModule {
    @Provides
    @IntoSet
    fun provideTopLevelNavItem(): TopLevelNavItem = TopLevelNavItem(
        route = InfoRoute.Info,
        icon = com.jinproject.design_ui.R.drawable.ic_setting,
        iconClicked = com.jinproject.design_ui.R.drawable.ic_setting_clicked,
        order = 4,
    )
}
