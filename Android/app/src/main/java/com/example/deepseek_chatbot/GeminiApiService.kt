package com.example.deepseek_chatbot

//package com.example.chatbot

//package com.example.chatbot

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object GeminiApiService {

    // Replace with your actual Gemini API key
    private const val API_KEY = "" // ENTER YOUR API KEY HERE

    // Initialize the GenerativeModel with the gemini-1.5-flash model
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", // Use the correct model name
        apiKey = API_KEY,
        generationConfig = generationConfig {
            temperature = 0.9f // Adjust as needed
            topK = 16 // Adjust as needed
        }
    )

    /**
     * Sends a message to the Gemini API and returns the response via a callback.
     *
     * @param message The user's message.
     * @param callback A lambda function to handle the response.
     */
    fun sendMessage(message: String, callback: (String) -> Unit) {
        // Use a coroutine to call the suspend function
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Generate content using the Gemini model
                val response = generativeModel.generateContent(message)
                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    callback(response.text ?: "No response from Gemini")
                }
            } catch (e: Exception) {
                // Switch to the main thread to handle errors
                withContext(Dispatchers.Main) {
                    callback("Error: ${e.message}")
                }
            }
        }
    }
}