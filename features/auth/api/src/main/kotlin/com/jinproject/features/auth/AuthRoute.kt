package com.jinproject.features.auth

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface AuthRoute : NavKey {
    @Serializable
    data object SignIn : AuthRoute
}
