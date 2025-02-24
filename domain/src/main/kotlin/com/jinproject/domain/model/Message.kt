package com.jinproject.domain.model

data class Message(
    val publisher: String,
    val data: String,
    val timeStamp: Long,
)
