package com.jinproject.design_compose.component.text.auth

interface AuthIdState {
    val id: AuthTextFieldState

    val email get() = id.textFieldState.text.toString()
}

interface AuthPasswordState {
    val pw: AuthTextFieldState
    val pwCheck: AuthTextFieldState?

    val password get() = pw.textFieldState.text.toString()
}