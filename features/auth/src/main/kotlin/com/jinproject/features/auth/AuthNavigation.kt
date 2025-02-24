package com.jinproject.features.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jinproject.features.auth.signin.SignInScreen
import com.jinproject.features.auth.signup.SignUpScreen
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.Route
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoute : Route {
    @Serializable
    data object AuthGraph : AuthRoute()

    @Serializable
    internal data object SignIn : AuthRoute()

    @Serializable
    internal data object SignUp : AuthRoute()
}

fun NavGraphBuilder.authNavigation(
    navigatePopBackStack: () -> Unit,
    navigatePopBackStackToRoute: (Route, Boolean) -> Unit,
    navigateAuthRoute: (AuthRoute) -> Unit,
    navigateToGenerateImage: (NavOptions?) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    isPreviousDestinationGenerateImage: () -> Boolean,
) {
    navigation<AuthRoute.AuthGraph>(startDestination = AuthRoute.SignIn) {
        composable<AuthRoute.SignIn> {
            SignInScreen(
                navigatePopBackStack = navigatePopBackStack,
                navigateAuthRoute = navigateAuthRoute,
                navigateToGenerateImage = navigateToGenerateImage,
                showSnackBar = showSnackBar,
                isPreviousDestinationGenerateImage = isPreviousDestinationGenerateImage,
            )
        }

        composable<AuthRoute.SignUp> {
            SignUpScreen(
                navigatePopBackStackToRoute = navigatePopBackStackToRoute,
                showSnackBar = showSnackBar,
            )
        }
    }
}

fun NavController.navigateToAuthGraph() {
    navigate(AuthRoute.AuthGraph)
}

fun NavController.navigateAuthRoute(route: AuthRoute) {
    navigate(route)
}