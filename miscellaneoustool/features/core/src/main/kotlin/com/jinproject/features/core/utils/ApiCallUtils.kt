package com.jinproject.features.core.utils

import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun getSnackBarMessageFromApiCall(
    crossinline apiCall: suspend () -> Unit,
    crossinline successMessage: suspend () -> SnackBarMessage,
    crossinline failMessage: (Throwable) -> SnackBarMessage,
): SnackBarMessage = runCatching {
    withContext(Dispatchers.IO) { apiCall() }
}.fold(
    onSuccess = {
        successMessage()
    },
    onFailure = { t ->
        failMessage(t)
    }
)