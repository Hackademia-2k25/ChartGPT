package com.example.deepseek_chatbot

//package com.example.chatbot

/**
 * Represents a chat message.
 *
 * @property text The content of the message.
 * @property isUser Whether the message is from the user (true) or the bot (false).
 */
data class Message(
    val text: String,
    val isUser: Boolean,
    val type: MessageType = MessageType.TEXT // Default to TEXT
)

enum class MessageType {
    TEXT,
    WEBVIEW
}