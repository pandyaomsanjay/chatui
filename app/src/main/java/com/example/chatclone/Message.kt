package com.example.chatclone

data class Message(
    val text: String = "",
    val isSent: Boolean,
    val timestamp: String,
    val isSeen: Boolean = false,
    val isDelivered: Boolean = true,
    val mediaType: Int = 0, // 0 = text, 1 = image, 2 = voice
    val mediaUrl: String? = null, // local URI or path
    val duration: String? = null // for voice
)