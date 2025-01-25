package com.example.deepseek_chatbot

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var topTextContainer: View
    private lateinit var textGetYour: TextView
    private lateinit var textIdeas: TextView
    private lateinit var textFlowing: TextView
    private val chatMessages = mutableListOf<Message>() // List to hold chat messages

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)

        // Initialize views
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)
        topTextContainer = findViewById(R.id.topTextContainer)
        textGetYour = findViewById(R.id.getyour)
        textIdeas = findViewById(R.id.ideas)
        textFlowing = findViewById(R.id.flowing)

        // Set up RecyclerView
        chatAdapter = ChatAdapter(chatMessages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        // Start the sequential fade-in animation
        startSequentialFadeInAnimation()

        // Send button click listener
        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString()
            if (userMessage.isNotEmpty()) {
                // Fade out the top text container
                fadeOutTopTextContainer()

                // Add user message to the chat
                chatMessages.add(Message(userMessage, true, MessageType.TEXT))
                chatAdapter.notifyItemInserted(chatMessages.size - 1)
                chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                messageEditText.text.clear()

                // Prepare the prompt for Gemini API
                val prompt = """
                    You are an assistant to help the user build diagrams with Mermaid.js. 
                    You should return only the Mermaid.js code. 
                    Do not include the \`\`\`mermaid\` block at the beginning or the closing \`\`\` at the end. 
                    Only return the Mermaid.js code in plain text without any additional explanations or text.
                    For example, if the prompt is "generate an algorithm explaining the steps of photosynthesis," you should return the Mermaid.js code for the diagram without any other formatting.
                    Also, do not use parentheses \`(\` or \`) \` in the code. Only use square brackets \`[and]\` for nodes or labels.
                    Code (no \`\`\` mermaid or \`\`\`):.$userMessage
                """.trimIndent()

                // Call Gemini API to get bot response
                GeminiApiService.sendMessage(prompt) { botResponse ->
                    // Add a text message: "Sure, here's the algorithm"
                    chatMessages.add(Message("Sure, here's the algorithm", false, MessageType.TEXT))
                    chatAdapter.notifyItemInserted(chatMessages.size - 1)
                    chatRecyclerView.scrollToPosition(chatMessages.size - 1)

                    // Wrap the Mermaid.js code in a proper HTML structure
                    val htmlContent = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>
                            <script>mermaid.initialize({ startOnLoad: true });</script>
                        </head>
                        <body>
                            <div class="mermaid">
                                $botResponse
                            </div>
                        </body>
                        </html>
                    """.trimIndent()

                    // Add bot response to the chat
                    chatMessages.add(Message(htmlContent, false, MessageType.WEBVIEW)) // Use WEBVIEW for Mermaid.js
                    chatAdapter.notifyItemInserted(chatMessages.size - 1)
                    chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                }
            }
        }
    }

    private fun startSequentialFadeInAnimation() {
        // Fade in "Get your" after 0ms delay
        textGetYour.animate()
            .alpha(1f)
            .setDuration(1500)
            .setStartDelay(0)
            .withEndAction {
                // Fade in "ideas" after "Get your" has finished fading in
                textIdeas.animate()
                    .alpha(1f)
                    .setDuration(900)
                    .setStartDelay(0)
                    .withEndAction {
                        // Fade in "flowing" after "ideas" has finished fading in
                        textFlowing.animate()
                            .alpha(1f)
                            .setDuration(600)
                            .setStartDelay(0)
                            .start()
                    }
                    .start()
            }
            .start()
    }

    private fun fadeOutTopTextContainer() {
        topTextContainer.animate()
            .alpha(0f)
            .setDuration(500)
            .withEndAction {
                topTextContainer.visibility = View.GONE
            }
            .start()
    }
}