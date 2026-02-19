package com.jinproject.features.auth.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.jinproject.design_compose.component.text.auth.AuthIdState
import com.jinproject.design_compose.component.text.auth.AuthPasswordState
import com.jinproject.design_compose.component.text.auth.AuthTextFieldState
import com.jinproject.design_compose.component.text.auth.rememberAuthTextFieldState
import com.jinproject.design_ui.R
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.getSnackBarMessageFromApiCall
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(FlowPreview::class)
@Composable
internal fun rememberSignUiState(): SignUiState {
    val id = rememberAuthTextFieldState()
    val password = rememberAuthTextFieldState()
    val passwordCheck = rememberAuthTextFieldState()

    val signUiState = remember {
        SignUiState(
            id = id,
            pw = password,
            pwCheck = passwordCheck,
        )
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { signUiState.pw.textFieldState.text }
            .distinctUntilChanged()
            .debounce(300)
            .filter { it.isNotBlank() && signUiState.pwCheck.textFieldState.text.isNotBlank() }
            .collectLatest { text ->
                signUiState.pwCheck.changeIsError(
                    !signUiState.pwCheck.textFieldState.text.contentEquals(
                        text
                    )
                )
            }
    }

    return signUiState
}

@Stable
internal data class SignUiState(
    override val id: AuthTextFieldState,
    override val pw: AuthTextFieldState,
    override val pwCheck: AuthTextFieldState,
) : AuthIdState, AuthPasswordState {
    private val sideEffectChannel = Channel<SignSideEffect>()
    val sideEffect = sideEffectChannel.receiveAsFlow()

    val isEnabled: Boolean
        get() = id.isNotErrorAndBlank && pw.isNotErrorAndBlank && pwCheck.isNotErrorAndBlank

    var isVerifying by mutableStateOf(false)
        private set

    fun changeIsVerifying(bool: Boolean) {
        isVerifying = bool
    }

    suspend fun signUp(context: Context) {
        val snackBarMessage = getSnackBarMessageFromApiCall(
            apiCall = {
                AuthManager.signUpFirebaseAuth(email = email, password = password)
            },
            successMessage = {
                getSnackBarMessageFromApiCall(
                    apiCall = {
                        AuthManager.sendEmailVerification()
                    },
                    successMessage = {
                        changeIsVerifying(true)

                        SnackBarMessage(
                            headerMessage = context.getString(R.string.auth_sign_send_email_verification),
                            contentMessage = context.getString(R.string.auth_sign_verify_authentication_on_email)
                        )
                    },
                    failMessage = { t -> throw t }
                )
            },
            failMessage = { t ->
                val contentMessage = when (t.cause) {
                    is FirebaseAuthUserCollisionException -> {
                        context.getString(R.string.auth_sign_email_already_exists)
                    }

                    else -> {
                        t.cause?.message ?: ""
                    }
                }

                SnackBarMessage(
                    headerMessage = context.getString(R.string.auth_sign_sign_up_failed),
                    contentMessage = contentMessage,
                )
            }
        )

        sideEffectChannel.send(
            SignSideEffect.ShowSnackBar(snackBarMessage)
        )
    }

    suspend fun signIn(context: Context) {
        var shouldNavigateAfterSignIn = false

        val snackBarMessage = getSnackBarMessageFromApiCall(
            apiCall = {
                AuthManager.signInFirebaseAuth(
                    email = email,
                    password = password,
                )
            },
            successMessage = {
                if (AuthManager.isEmailVerified) {
                    shouldNavigateAfterSignIn = true

                    SnackBarMessage(
                        headerMessage = context.getString(R.string.auth_sign_in_success),
                    )
                } else {
                    SnackBarMessage(
                        headerMessage = context.getString(R.string.auth_sign_email_not_verified),
                        contentMessage = context.getString(R.string.auth_sign_verify_authentication_on_email),
                    )
                }
            },
            failMessage = { t ->
                SnackBarMessage(
                    headerMessage = context.getString(R.string.auth_sign_in_failed),
                    contentMessage = t.cause?.message ?: ""
                )

            }
        )

        sideEffectChannel.send(
            SignSideEffect.ShowSnackBar(snackBarMessage)
        )

        if (shouldNavigateAfterSignIn) {
            sideEffectChannel.send(SignSideEffect.NavigateAfterSignInSuccess)
        }
    }

    suspend fun withdrawal() = AuthManager.deleteUser(password)
}

internal sealed class SignSideEffect {
    data class ShowSnackBar(val message: SnackBarMessage) : SignSideEffect()
    data object NavigateAfterSignInSuccess : SignSideEffect()
}
