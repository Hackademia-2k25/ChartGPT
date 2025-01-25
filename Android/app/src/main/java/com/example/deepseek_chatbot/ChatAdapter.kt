package com.example.deepseek_chatbot

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_TEXT = 1
        private const val VIEW_TYPE_WEBVIEW = 2
        private const val VIEW_TYPE_SPECIAL_TEXT = 3 // New view type for the special message
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            messages[position].type == MessageType.WEBVIEW -> VIEW_TYPE_WEBVIEW
            messages[position].text == "Sure, here's the algorithm" -> VIEW_TYPE_SPECIAL_TEXT // Special case
            else -> VIEW_TYPE_TEXT // Default text message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TEXT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
                TextMessageViewHolder(view)
            }
            VIEW_TYPE_WEBVIEW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_webview, parent, false)
                WebViewViewHolder(view)
            }
            VIEW_TYPE_SPECIAL_TEXT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message2, parent, false)
                SpecialTextMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is TextMessageViewHolder -> holder.bind(message)
            is WebViewViewHolder -> holder.bind(message)
            is SpecialTextMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    // ViewHolder for regular text messages
    class TextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: Message) {
            messageText.text = message.text
            messageText.setBackgroundResource(
                if (message.isUser) R.drawable.user_message_bg else R.drawable.bot_message_bg
            )
        }
    }

    // ViewHolder for WebView messages
    class WebViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val botWebView: WebView = itemView.findViewById(R.id.botWebView)

        fun bind(message: Message) {
            botWebView.settings.javaScriptEnabled = true // Enable JavaScript for Mermaid.js
            botWebView.settings.domStorageEnabled = true // Enable DOM storage for Mermaid.js
            botWebView.loadDataWithBaseURL(
                null, // Use null for base URL
                message.text,
                "text/html", // MIME type
                "UTF-8", // Encoding
                null // History URL (optional)
            )
        }
    }

    // ViewHolder for the special text message
    class SpecialTextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: Message) {
            messageText.text = message.text
            // Apply any custom styling for the special message here
            messageText.setBackgroundResource(R.drawable.user_message_bg) // Example background
            messageText.setTextColor(Color.BLACK) // Example text color
        }
    }
}