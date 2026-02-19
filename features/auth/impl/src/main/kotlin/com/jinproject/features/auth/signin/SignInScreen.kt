package com.jinproject.features.auth.signin

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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
import com.jinproject.design_compose.utils.clearFocusIfKeyboardActive
import com.jinproject.design_ui.R
import com.jinproject.features.auth.AuthRoute
import com.jinproject.features.auth.SignUp
import com.jinproject.features.auth.state.SignSideEffect
import com.jinproject.features.auth.state.SignUiState
import com.jinproject.features.auth.state.rememberSignUiState
import com.jinproject.features.core.compose.LocalNavigator
import com.jinproject.features.core.compose.LocalShowSnackbar
import com.jinproject.features.symbol.SymbolRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
internal fun SignInScreen() {
    val navigator = LocalNavigator.current
    val showSnackBar = LocalShowSnackbar.current

    val navigateExitLogin = {
        val currentBackStack = navigator.currentBackStack
        if (currentBackStack.size >= 2 && currentBackStack[currentBackStack.size - 2] is SymbolRoute.GenerateImage)
            navigator.replaceAbove(AuthRoute.SignIn, SymbolRoute.GenerateImage)
        else
            navigator.goBack()
    }

    val signUiState = rememberSignUiState()

    LaunchedEffect(signUiState) {
        signUiState.sideEffect.collectLatest { effect ->
            when (effect) {
                is SignSideEffect.ShowSnackBar -> showSnackBar(effect.message)
                is SignSideEffect.NavigateAfterSignInSuccess -> navigateExitLogin()
            }
        }
    }

    SignInScreen(
        signUiState = signUiState,
        navigateOnBack = navigateExitLogin,
        navigateToSignUp = { navigator.navigate(SignUp) }
    )
}

@Composable
private fun SignInScreen(
    signUiState: SignUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    context: Context = LocalContext.current,
    navigateOnBack: () -> Unit,
    navigateToSignUp: () -> Unit,
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie/login.json"))
    val lottieProgress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    DefaultLayout(
        modifier = Modifier
            .clearFocusIfKeyboardActive(),
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = navigateOnBack,
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

        VerticalSpacer(20.dp)
        EmailField(authTextFieldState = signUiState.id)
        VerticalSpacer(8.dp)
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
                    signUiState.signIn(context = context)
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
                modifier = Modifier.clickableAvoidingDuplication(onClick = navigateToSignUp),
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
        signUiState = rememberSignUiState(),
        navigateOnBack = {},
        navigateToSignUp = {},
    )
}
