package com.jinproject.data.repository

import com.jinproject.data.ChatMessage
import com.jinproject.data.datasource.cache.CollectionDataStorePreferences
import com.jinproject.data.datasource.remote.GenerateImageDataSource
import com.jinproject.data.di.ImageDownloadManager
import com.jinproject.domain.model.Message
import com.jinproject.domain.repository.SymbolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SymbolRepositoryImpl @Inject constructor(
    private val collectionDataStorePreferences: CollectionDataStorePreferences,
    private val generateImageDataSource: GenerateImageDataSource,
    private val imageDownloadManager: ImageDownloadManager,
) : SymbolRepository {
    override suspend fun addPaidSymbol(uri: String) {
        collectionDataStorePreferences.addPaidSymbol(uri)
    }

    override fun getPaidSymbolUris(): Flow<List<String>> =
        collectionDataStorePreferences.getPaidSymbolUris()


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

        generateImageDataSource.generateImage(
            "Can you generate a logo with below detail? " +
                    "1. ${prompt}. " +
                    "2. The background color of logo must be white which is 0xFFFFFFFF in hexadecimal color code.  " +
                    "3. The entire logo must be 12*12 pixels in size and must be optimized to be clear and not blurry. "
        )
            ?.let { response ->
                replaceChat(
                    Message(
                        publisher = "AI",
                        data = response.data.first().url,
                        timeStamp = responseTimeStamp,
                    )
                )
            } ?: throw IllegalStateException("응답이 비어있습니다.")
    }

    override fun getChatList(): Flow<List<Message>> =
        collectionDataStorePreferences.getChatMessage().map { chatMessages ->
            chatMessages.map { message ->
                Message(
                    publisher = message.publisher,
                    data = message.data,
                    timeStamp = message.timestamp,
                )
            }
        }

    override suspend fun addChat(message: Message) {
        collectionDataStorePreferences.addChat(
            ChatMessage.newBuilder()
                .setPublisher(message.publisher)
                .setData(message.data)
                .setTimestamp(message.timeStamp)
                .build()
        )
    }

    override suspend fun replaceChat(message: Message) {
        collectionDataStorePreferences.replaceChatMessage(
            url = message.data,
            timeStamp = message.timeStamp
        )
    }

    override suspend fun downloadImage(url: String, timeStamp: Long) {
        imageDownloadManager.execute(url = url, timeStamp = timeStamp)
    }

}