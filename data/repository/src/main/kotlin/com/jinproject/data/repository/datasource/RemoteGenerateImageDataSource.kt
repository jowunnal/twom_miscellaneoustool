package com.jinproject.data.repository.datasource

interface RemoteGenerateImageDataSource {
    suspend fun getGeneratedImageUrl(prompt: String): String?
}