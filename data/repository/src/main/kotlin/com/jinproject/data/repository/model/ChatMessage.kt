package com.jinproject.data.repository.model

import com.jinproject.domain.model.Message

data class ChatMessage(
    val publisher: String,
    val data: String,
    val timeStamp: Long,
)

fun Message.toChatMessage() = ChatMessage(
    publisher = publisher,
    data = data,
    timeStamp = timeStamp
)
