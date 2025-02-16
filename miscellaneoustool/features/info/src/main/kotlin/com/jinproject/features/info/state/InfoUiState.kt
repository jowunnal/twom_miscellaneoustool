package com.jinproject.features.info.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.jinproject.design_compose.component.text.auth.AuthPasswordState
import com.jinproject.design_compose.component.text.auth.AuthTextFieldState
import com.jinproject.design_compose.component.text.auth.rememberAuthTextFieldState
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.getSnackBarMessageFromApiCall
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(FlowPreview::class)
@Composable
internal fun rememberInfoUiState(
    isUserActive: Boolean,
    showSnackBar: (SnackBarMessage) -> Unit,
    pw: AuthTextFieldState = rememberAuthTextFieldState(),
    pwCheck: AuthTextFieldState? = rememberAuthTextFieldState(),
): InfoUiState {

    val infoUiState = remember {
        InfoUiState(
            isUserActive = isUserActive,
            pw = pw,
            pwCheck = pwCheck,
            showSnackBar = showSnackBar,
        )
    }

    LaunchedEffect(key1 = Unit) {
        infoUiState.pwCheck?.let {
            snapshotFlow { infoUiState.pw.textFieldState.text }
                .distinctUntilChanged()
                .debounce(300)
                .filter { it.isNotBlank() && (infoUiState.pwCheck.textFieldState.text.isNotBlank()) }
                .collectLatest { text ->
                    infoUiState.pwCheck.changeIsError(
                        !infoUiState.pwCheck.textFieldState.text.contentEquals(
                            text
                        )
                    )
                }
        }
    }

    return infoUiState
}

@Stable
internal data class InfoUiState(
    private val isUserActive: Boolean,
    override val pw: AuthTextFieldState,
    override val pwCheck: AuthTextFieldState?,
    val showSnackBar: (SnackBarMessage) -> Unit,
) : AuthPasswordState {
    val enabled
        get() = pwCheck?.let {
            pw.isNotErrorAndBlank && pwCheck.isNotErrorAndBlank
        } ?: pw.isNotErrorAndBlank

    var isActive by mutableStateOf(isUserActive)
        private set

    fun changeIsActive(bool: Boolean) {
        isActive = bool
    }

    suspend fun updateUserInfo(onSuccess: () -> Unit) {
        val snackBarMessage = getSnackBarMessageFromApiCall(
            apiCall = {
                AuthManager.updatePassword(pw.textFieldState.text.toString())
            },
            successMessage = {
                reAuthenticate()
                onSuccess()
                SnackBarMessage(
                    headerMessage = "회원 정보가 변경되었어요.",
                )
            },
            failMessage = { t ->
                SnackBarMessage(
                    headerMessage = "회원 정보의 변경에 실패했어요.",
                    contentMessage = "[${t.message} 에 의해 실패했어요."
                )
            }
        )

        showSnackBar(
            snackBarMessage
        )
    }

    private suspend fun reAuthenticate() = AuthManager.reAuthenticate(
        password = pw.textFieldState.text.toString(),
    )

    suspend fun checkUserPassword(onSuccess: () -> Unit) {
        val snackBarMessage = getSnackBarMessageFromApiCall(
            apiCall = { reAuthenticate() },
            successMessage = {
                onSuccess()
                SnackBarMessage.getInitValues()
            },
            failMessage = {
                SnackBarMessage(
                    headerMessage = "비밀번호가 일치하지 않아요."
                )
            }
        )

        showSnackBar(snackBarMessage)
    }

    suspend fun logOut() {
        val snackBarMessage = getSnackBarMessageFromApiCall(
            apiCall = {
                AuthManager.signOut()
            },
            successMessage = {
                SnackBarMessage(
                    headerMessage = "로그아웃 되었어요.",
                )
            },
            failMessage = { t ->
                SnackBarMessage(
                    headerMessage = "로그아웃에 실패했어요.",
                    contentMessage = t.message.toString(),
                )

            }
        )

        showSnackBar(snackBarMessage)
    }

    suspend fun withdrawal() {
        val snackBarMessage = if (enabled) {
            getSnackBarMessageFromApiCall(
                apiCall = {
                    reAuthenticate()
                },
                successMessage = {
                    AuthManager.deleteUser(password)
                    SnackBarMessage(
                        headerMessage = "성공적으로 탈퇴 되었어요.",
                    )
                },
                failMessage = { t ->
                    SnackBarMessage(
                        headerMessage = "회원탈퇴에 실패했어요.",
                        contentMessage = t.message.toString(),
                    )
                }
            )
        } else
            SnackBarMessage(
                headerMessage = "회원탈퇴에 실패했어요.",
                contentMessage = "비밀번호가 형식에 어긋나요."
            )

        showSnackBar(snackBarMessage)
    }

}