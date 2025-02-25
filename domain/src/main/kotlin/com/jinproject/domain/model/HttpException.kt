package com.jinproject.domain.model

class HttpException(
    override val message: String,
    val code: Int,
) : Exception(message)