package com.jinproject.data.repository.datasource

interface RemoteImageDownloadManager {
    suspend fun execute(url: String, timeStamp: Long)
}