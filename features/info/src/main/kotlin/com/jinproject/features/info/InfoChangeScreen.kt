package com.jinproject.features.info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.DescriptionMediumText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.design_compose.component.text.auth.PasswordField
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.info.state.InfoUiState
import com.jinproject.features.info.state.rememberInfoUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun InfoChangeScreen(
    showSnackBar: (SnackBarMessage) -> Unit,
    navigatePopBackStack: () -> Unit,
) {
    val infoUiState =
        rememberInfoUiState(isUserActive = AuthManager.isActive, showSnackBar = showSnackBar)

    InfoChangeScreen(
        infoUiState = infoUiState,
        navigatePopBackStack = navigatePopBackStack,
    )
}

@Composable
private fun InfoChangeScreen(
    infoUiState: InfoUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    focusManager: FocusManager = LocalFocusManager.current,
    navigatePopBackStack: () -> Unit,
) {
    DefaultLayout(
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = navigatePopBackStack,
                title = stringResource(id = R.string.change_info),
            )
        },
        contentPaddingValues = MiscellanousToolPaddingValues(horizontal = 12.dp, vertical = 20.dp)
    ) {
        DescriptionMediumText(
            text = stringResource(id = R.string.info_change_password),
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        PasswordField(
            authTextFieldState = infoUiState.pw,
        )
        PasswordField(
            modifier = Modifier,
            authTextFieldState = infoUiState.pwCheck!!,
            hint = stringResource(id = R.string.auth_sign_repeat_password),
            checkIsError = { text ->
                !infoUiState.pw.textFieldState.text.contentEquals(text)
            },
            errorMessage = stringResource(id = R.string.auth_sign_repeat_password_error),
        )

        VerticalSpacer(height = 20.dp)

        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.apply_do),
            contentPaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            enabled = infoUiState.enabled,
            onClick = {
                coroutineScope.launch {
                    focusManager.clearFocus()
                    infoUiState.updateUserInfo(
                        onSuccess = {
                            navigatePopBackStack()
                        }
                    )
                }
            },
        )

        Spacer(modifier = Modifier.weight(1f))
    }


}

@Composable
@Preview
private fun PreviewInfoChangeScreen() = MiscellaneousToolTheme {
    InfoChangeScreen(
        infoUiState = rememberInfoUiState(isUserActive = true, showSnackBar = {}),
        navigatePopBackStack = {},
    )
}