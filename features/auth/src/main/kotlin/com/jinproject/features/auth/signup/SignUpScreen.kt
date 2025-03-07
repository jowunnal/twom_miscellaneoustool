package com.jinproject.features.auth.signup

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.utils.clearFocusIfKeyboardActive
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.HeadlineText
import com.jinproject.design_compose.component.text.auth.EmailField
import com.jinproject.design_compose.component.text.auth.PasswordField
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.auth.AuthRoute
import com.jinproject.features.auth.state.SignUiState
import com.jinproject.features.auth.state.rememberSignUiState
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun SignUpScreen(
    showSnackBar: (SnackBarMessage) -> Unit,
    signUiState: SignUiState = rememberSignUiState(showSnackBar = showSnackBar),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    context: Context = LocalContext.current,
    navigatePopBackStackToRoute: (Route, Boolean) -> Unit,
) {
    DefaultLayout(
        modifier = Modifier
            .clearFocusIfKeyboardActive(),
        contentPaddingValues = MiscellanousToolPaddingValues(horizontal = 12.dp),
    ) {
        HeadlineText(
            text = stringResource(id = R.string.auth_sign_create_account),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp),
            textAlign = TextAlign.Center,
        )
        EmailField(
            authTextFieldState = signUiState.id,
            enabled = !signUiState.isVerifying,
        )
        PasswordField(
            authTextFieldState = signUiState.pw,
            enabled = !signUiState.isVerifying,
        )
        PasswordField(
            modifier = Modifier,
            authTextFieldState = signUiState.pwCheck,
            hint = stringResource(id = R.string.auth_sign_repeat_password),
            checkIsError = { text ->
                !signUiState.pw.textFieldState.text.contentEquals(text)
            },
            errorMessage = stringResource(id = R.string.auth_sign_repeat_password_error),
            enabled = !signUiState.isVerifying,
        )
        VerticalSpacer(height = 20.dp)

        AnimatedVisibility(visible = !signUiState.isVerifying) {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.auth_sign_email_verify),
                contentPaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                enabled = signUiState.isEnabled,
                onClick = {
                    softwareKeyboardController?.hide()

                    coroutineScope.launch {
                        signUiState.signUp(
                            context = context,
                            onSuccessEmailVerification = {
                                signUiState.changeIsVerifying(true)
                            }
                        )
                    }
                },
            )
        }

        AnimatedVisibility(visible = signUiState.isVerifying) {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.auth_sign_verify_authentication),
                contentPaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                enabled = signUiState.isEnabled,
                onClick = {
                    softwareKeyboardController?.hide()

                    coroutineScope.launch {
                        signUiState.signIn(
                            context = context,
                            onSuccess = {
                                navigatePopBackStackToRoute(AuthRoute.SignIn, true)
                            }
                        )
                    }
                },
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview
private fun PreviewSignUpScreen() = MiscellaneousToolTheme {
    SignUpScreen(
        navigatePopBackStackToRoute = { _, _ -> },
        showSnackBar = {},
    )
}