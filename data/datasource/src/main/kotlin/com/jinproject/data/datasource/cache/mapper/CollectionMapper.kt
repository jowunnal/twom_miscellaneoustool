package com.jinproject.data.datasource.cache.mapper

import com.jinproject.data.ChatMessage

fun ChatMessage.toDataModel() = com.jinproject.data.repository.model.ChatMessage(
    publisher = publisher,
    data = data,
    timeStamp = timestamp,
)

fun List<ChatMessage>.toDataModels() = map { it.toDataModel() }

fun com.jinproject.data.repository.model.ChatMessage.toChatMessage() = ChatMessage.newBuilder().apply {
    publisher = this@toChatMessage.publisher
    data = this@toChatMessage.data
    timestamp = this@toChatMessage.timeStamp
}.build()