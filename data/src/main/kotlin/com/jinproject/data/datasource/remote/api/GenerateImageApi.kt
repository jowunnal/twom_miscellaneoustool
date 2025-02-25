package com.jinproject.data.datasource.remote.api

import com.jinproject.data.datasource.remote.request.CreateImageRequest
import com.jinproject.data.datasource.remote.response.CreateImageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GenerateImageApi {

    @POST("images/generations")
    suspend fun generateImage(
        @Body createImageRequest: CreateImageRequest,
    ): Response<CreateImageResponse>
}