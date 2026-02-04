package com.jinproject.design_compose.component.text.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.text.OutlineVerifyTextField
import com.jinproject.design_compose.component.text.PasswordOutputTransformation
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import java.util.regex.Pattern

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    authTextFieldState: AuthTextFieldState = rememberAuthTextFieldState(),
    pattern: String = SignType.Email.pattern,
    enabled: Boolean = true,
) {
    SignField(
        modifier = modifier,
        authTextFieldState = authTextFieldState,
        pattern = pattern,
        headerIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_email),
                contentDescription = "Email",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(4.dp)
            )
        },
        errorMessage = stringResource(id = R.string.auth_sign_email_error),
        hint = stringResource(id = R.string.auth_sign_email),
        enabled = enabled,
    )
}



@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    authTextFieldState: AuthTextFieldState = rememberAuthTextFieldState(),
    hint: String = stringResource(id = R.string.auth_sign_password),
    pattern: String = SignType.Password.pattern,
    checkIsError: (CharSequence) -> Boolean = { text -> !Pattern.matches(pattern, text) },
    errorMessage: String = stringResource(id = R.string.auth_sign_password_error),
    enabled: Boolean = true,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    SignField(
        modifier = modifier.onFocusChanged { focusState -> isFocused = focusState.hasFocus },
        authTextFieldState = authTextFieldState,
        pattern = pattern,
        errorMessage = errorMessage,
        headerIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                contentDescription = "Password",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(4.dp)
            )
        },
        outputTransformation = PasswordOutputTransformation(hasFocus = isFocused),
        hint = hint,
        checkIsError = checkIsError,
        enabled = enabled,
    )
}

internal sealed interface SignType {
    val pattern: String

    data object Email : SignType {
        override val pattern: String
            get() = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
    }

    data object Password : SignType {
        override val pattern: String
            get() = "^(?=.*[a-z])(?=.*[0-9])(?=.*[_*\$@!])[A-Za-z0-9_*@\$!]{8,}\$"
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun SignField(
    modifier: Modifier = Modifier,
    authTextFieldState: AuthTextFieldState = rememberAuthTextFieldState(),
    pattern: String,
    errorMessage: String,
    headerIcon: @Composable (() -> Unit)? = null,
    outputTransformation: OutputTransformation? = null,
    hint: String = "",
    enabled: Boolean = true,
    checkIsError: (CharSequence) -> Boolean = { text -> !Pattern.matches(pattern, text) },
) {
    LaunchedEffect(Unit) {
        snapshotFlow { authTextFieldState.textFieldState.text }
            .distinctUntilChanged()
            .debounce(300)
            .filter { it.isNotBlank() }
            .collectLatest { text ->
                authTextFieldState.changeIsError(checkIsError(text))
            }
    }

    OutlineVerifyTextField(
        modifier = modifier,
        textFieldState = authTextFieldState.textFieldState,
        isError = authTextFieldState.isError,
        errorMessage = errorMessage,
        headerIcon = headerIcon,
        outputTransformation = outputTransformation,
        hint = hint,
        enabled = enabled,
    )
}

@Composable
@Preview
private fun PreviewEmailFieldScreen() = MiscellaneousToolTheme {
    Column(modifier = Modifier.fillMaxWidth()) {
        EmailField(
            authTextFieldState = AuthTextFieldState(TextFieldState("abcd")),
        )
    }
}

@Composable
@Preview
private fun PreviewPasswordFieldScreen() = MiscellaneousToolTheme {
    Column(modifier = Modifier.fillMaxWidth()) {
        PasswordField(
            authTextFieldState = AuthTextFieldState(TextFieldState("abcd")),
        )
    }
}

@Composable
@Preview
private fun PreviewSignFieldScreen() = MiscellaneousToolTheme {
    SignField(
        authTextFieldState = AuthTextFieldState(TextFieldState("abcd@gmail.com")),
        errorMessage = "올바른 이메일 양식이 아니에요.",
        pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
    )
}