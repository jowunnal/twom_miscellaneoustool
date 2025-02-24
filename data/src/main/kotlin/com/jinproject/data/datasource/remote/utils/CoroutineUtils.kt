package com.jinproject.data.datasource.remote.utils

import com.jinproject.domain.model.HttpException
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

suspend fun <R> convertResponseToResult(getResponse: suspend () -> Response<R>): R? = coroutineScope {
    val response = getResponse()

    if(!response.isSuccessful)
        throw HttpException(message = response.message(), code = response.code())
    else
        response.body()
}