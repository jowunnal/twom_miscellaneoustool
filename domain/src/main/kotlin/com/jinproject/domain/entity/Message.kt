package com.jinproject.domain.entity

data class Message(
    val publisher: String,
    val data: String,
    val timeStamp: Long,
)