package com.jinproject.data.datasource.remote

import com.jinproject.data.datasource.remote.api.GenerateImageApi
import com.jinproject.data.datasource.remote.request.CreateImageRequest
import com.jinproject.data.datasource.remote.utils.convertResponseToResult
import com.jinproject.data.repository.datasource.RemoteGenerateImageDataSource
import javax.inject.Inject

class GenerateImageDataSourceImpl @Inject constructor(
    private val generateImageApi: GenerateImageApi,
): RemoteGenerateImageDataSource {
    override suspend fun getGeneratedImageUrl(prompt: String): String? = convertResponseToResult {
        generateImageApi.generateImage(CreateImageRequest(prompt = prompt))
    }?.data?.first()?.url
}