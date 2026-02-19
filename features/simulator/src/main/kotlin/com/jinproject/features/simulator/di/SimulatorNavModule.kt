package com.jinproject.features.simulator.di

import com.jinproject.features.core.compose.TopLevelNavItem
import com.jinproject.features.simulator.SimulatorRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object SimulatorNavModule {
    @Provides
    @IntoSet
    fun provideTopLevelNavItem(): TopLevelNavItem = TopLevelNavItem(
        route = SimulatorRoute.Simulator,
        icon = com.jinproject.design_ui.R.drawable.icon_simulator,
        iconClicked = com.jinproject.design_ui.R.drawable.icon_simulator,
        order = 1,
    )
}
