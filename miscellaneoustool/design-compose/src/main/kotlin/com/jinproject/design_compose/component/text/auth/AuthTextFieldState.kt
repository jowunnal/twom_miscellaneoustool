package com.jinproject.design_compose.component.text.auth

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun rememberAuthTextFieldState(initialText: String = ""): AuthTextFieldState {
    val textFieldState: TextFieldState = rememberTextFieldState(initialText)

    return AuthTextFieldState(textFieldState)
}

@Stable
class AuthTextFieldState(
    val textFieldState: TextFieldState,
) {
    var isError by mutableStateOf(false)
        private set

    val isNotErrorAndBlank get() = !isError && textFieldState.text.isNotBlank()

    fun changeIsError(bool: Boolean) {
        isError = bool
    }
}
