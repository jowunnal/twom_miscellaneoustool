package com.jinproject.data.repository.repo

import com.jinproject.data.CollectionPreferences
import com.jinproject.data.repository.datasource.CacheCollectionDataSource
import com.jinproject.data.repository.datasource.RemoteGenerateImageDataSource
import com.jinproject.data.repository.datasource.RemoteImageDownloadManager
import com.jinproject.data.repository.model.toChatMessage
import com.jinproject.domain.model.Message
import com.jinproject.domain.repository.SymbolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SymbolRepositoryImpl @Inject constructor(
    private val cacheCollectionDataStorePreferences: CacheCollectionDataSource<CollectionPreferences>,
    private val remoteGenerateImageDataSource: RemoteGenerateImageDataSource,
    private val imageDownloadManager: RemoteImageDownloadManager,
) : SymbolRepository {
    override suspend fun addPaidSymbol(uri: String) {
        cacheCollectionDataStorePreferences.addPaidSymbol(uri)
    }

    override fun getPaidSymbolUris(): Flow<List<String>> =
        cacheCollectionDataStorePreferences.getPaidSymbolUris()


    override suspend fun generateSymbolImage(prompt: String) {
        addChat(
            Message(
                publisher = "User",
                data = prompt,
                timeStamp = System.currentTimeMillis(),
            )
        )
        val responseTimeStamp = System.currentTimeMillis()
        addChat(
            Message(
                publisher = "AI",
                data = "downloading",
                timeStamp = responseTimeStamp,
            )
        )

        remoteGenerateImageDataSource.getGeneratedImageUrl(
            "Create a simple and minimal symbol based on the following description: " +
                    "${prompt}.\n And the details are based on following description: " +
                    "1. The background must be pure white (#FFFFFF, 0xFFFFFFFF in hexadecimal color code).\n" +
                    "2. Do not include any additional elements such as color palettes, swatches, labels, or extra decorations.\n" +
                    "3. The logo should be sharp and clear, without unnecessary blurriness or artifacts.\n" +
                    "4. The image should only contain **the logo itself, without any extra graphical elements."
        )
            ?.let { url ->
                replaceChat(
                    Message(
                        publisher = "AI",
                        data = url,
                        timeStamp = responseTimeStamp,
                    )
                )
            } ?: throw IllegalStateException("응답이 비어있습니다.")
    }

    override fun getChatList(): Flow<List<Message>> =
        cacheCollectionDataStorePreferences.getChatMessage().map { chatMessages ->
            chatMessages.map { message ->
                Message(
                    publisher = message.publisher,
                    data = message.data,
                    timeStamp = message.timeStamp,
                )
            }
        }

    override suspend fun addChat(message: Message) {
        cacheCollectionDataStorePreferences.addChat(message.toChatMessage())
    }

    override suspend fun replaceChat(message: Message) {
        cacheCollectionDataStorePreferences.replaceChatMessage(
            url = message.data,
            timeStamp = message.timeStamp
        )
    }

    override suspend fun downloadImage(url: String, timeStamp: Long) {
        imageDownloadManager.execute(url = url, timeStamp = timeStamp)
    }
}