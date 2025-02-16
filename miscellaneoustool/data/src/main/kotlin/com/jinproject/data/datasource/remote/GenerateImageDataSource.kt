package com.jinproject.data.datasource.remote

import com.jinproject.data.datasource.remote.api.GenerateImageApi
import com.jinproject.data.datasource.remote.request.CreateImageRequest
import com.jinproject.data.datasource.remote.response.CreateImageResponse
import com.jinproject.data.datasource.remote.utils.convertResponseToResult
import javax.inject.Inject

class GenerateImageDataSource @Inject constructor(
    private val generateImageApi: GenerateImageApi,
) {
    suspend fun generateImage(prompt: String): CreateImageResponse? = convertResponseToResult {
        generateImageApi.generateImage(CreateImageRequest(prompt = prompt))
    }
}