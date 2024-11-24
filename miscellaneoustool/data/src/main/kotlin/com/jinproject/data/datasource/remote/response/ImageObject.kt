package com.jinproject.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

data class ImageObject(
    val url: String,
    @SerializedName("revised_prompt")val revisedPrompt: String
)