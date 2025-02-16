package com.jinproject.data.datasource.remote.request

import com.google.gson.annotations.SerializedName

data class CreateImageRequest(
    val prompt: String,
    val model: String = "dall-e-3",
    @SerializedName("n") val number: Int = 1,
    @SerializedName("response_format") val responseFormat: String = "url",
    val quality: String = "standard",
    val size: String = "1024x1024",
    val style: String = "natural",
)
