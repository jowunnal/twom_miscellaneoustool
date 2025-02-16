package com.jinproject.data.datasource.remote.response

data class CreateImageResponse(
    val created: Long,
    val data: List<ImageObject>,
)
