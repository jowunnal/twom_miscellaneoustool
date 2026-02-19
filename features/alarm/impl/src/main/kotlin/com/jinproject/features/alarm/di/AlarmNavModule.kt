package com.jinproject.features.alarm.di

import com.jinproject.features.alarm.AlarmRoute
import com.jinproject.features.core.compose.TopLevelNavItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object AlarmNavModule {
    @Provides
    @IntoSet
    fun provideTopLevelNavItem(): TopLevelNavItem = TopLevelNavItem(
        route = AlarmRoute.Alarm,
        icon = com.jinproject.design_ui.R.drawable.icon_alarm,
        iconClicked = com.jinproject.design_ui.R.drawable.icon_alarm,
        order = 3,
    )
}
