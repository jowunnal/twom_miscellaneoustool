package com.jinproject.data.repository.model

class HttpException(
    override val message: String,
    val code: Int,
) : Exception(message)