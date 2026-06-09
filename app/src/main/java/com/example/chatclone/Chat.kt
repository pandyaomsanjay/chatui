package com.example.chatclone

data class Chat(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val avatarRes: Int = R.drawable.avatar_placeholder,
    val isOnline: Boolean = false,
    var unreadCount: Int = 0
)