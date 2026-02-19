package com.jinproject.features.info

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope
import com.jinproject.features.core.BillingModule
import kotlinx.serialization.Serializable

@Serializable
sealed class InfoRoute : NavKey {
    @Serializable
    data object Info : InfoRoute()

    @Serializable
    internal data object InfoChange : InfoRoute()

    @Serializable
    internal data object Term : InfoRoute()
}

fun EntryProviderScope<NavKey>.infoEntries(
    billingModule: BillingModule,
) {
    entry<InfoRoute.Info> {
        InfoScreen(
            billingModule = billingModule,
        )
    }

    entry<InfoRoute.InfoChange> { InfoChangeScreen() }

    entry<InfoRoute.Term> { TermScreen() }
}
