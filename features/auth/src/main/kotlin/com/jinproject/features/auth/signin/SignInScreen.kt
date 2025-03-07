package com.jinproject.features.auth.signin

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jinproject.design_compose.utils.clearFocusIfKeyboardActive
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.lottie.PreviewLottieAnimation
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.component.text.auth.EmailField
import com.jinproject.design_compose.component.text.auth.PasswordField
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.auth.AuthRoute
import com.jinproject.features.auth.state.rememberSignUiState
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun SignInScreen(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    context: Context = LocalContext.current,
    navigatePopBackStack: () -> Unit,
    navigateAuthRoute: (AuthRoute) -> Unit,
    navigateToGenerateImage: (NavOptions?) -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    isPreviousDestinationGenerateImage: () -> Boolean,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie/login.json"))
    val lottieProgress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    val signUiState = rememberSignUiState(showSnackBar = showSnackBar)
    val navigateExitLogin = {
        if (isPreviousDestinationGenerateImage())
            navigateToGenerateImage(navOptions { popUpTo(AuthRoute.SignIn) { inclusive = true } })
        else
            navigatePopBackStack()
    }

    DefaultLayout(
        modifier = Modifier
            .clearFocusIfKeyboardActive(),
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = navigateExitLogin,
                title = stringResource(id = R.string.login),
            )
        },
        contentPaddingValues = MiscellanousToolPaddingValues(horizontal = 12.dp),
    ) {
        PreviewLottieAnimation(
            composition = composition,
            progress = lottieProgress,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        EmailField(authTextFieldState = signUiState.id)
        PasswordField(authTextFieldState = signUiState.pw)
        VerticalSpacer(height = 20.dp)
        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.login),
            contentPaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            onClick = {
                coroutineScope.launch {
                    softwareKeyboardController?.hide()
                    signUiState.signIn(
                        context = context,
                        onSuccess = navigateExitLogin
                    )
                }
            },
        )

        VerticalSpacer(height = 12.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
        ) {
            DescriptionSmallText(
                text = stringResource(id = R.string.auth_sign_no_account),
            )
            HorizontalSpacer(width = 4.dp)
            DescriptionSmallText(
                modifier = Modifier.clickableAvoidingDuplication {
                    navigateAuthRoute(AuthRoute.SignUp)
                },
                text = stringResource(id = R.string.sign_up),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                ),
            )
        }
    }
}

@Composable
@Preview
private fun PreviewSignInScreen() = MiscellaneousToolTheme {
    SignInScreen(
        navigatePopBackStack = {},
        navigateAuthRoute = {},
        navigateToGenerateImage = {},
        showSnackBar = {},
        isPreviousDestinationGenerateImage = { false },
    )
}