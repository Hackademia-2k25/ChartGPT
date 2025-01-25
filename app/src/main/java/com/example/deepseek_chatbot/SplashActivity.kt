package com.example.deepseek_chatbot


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val videoView: VideoView = findViewById(R.id.splashVideoView)

        // Set the video URI
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splash}")
        videoView.setVideoURI(videoUri)

        // Start playing the video
        videoView.start()

        // Listen for video completion or set a delay
        videoView.setOnCompletionListener { navigateToMain() }

        // Add a fallback delay of 5 seconds in case the video doesn't trigger completion
        videoView.postDelayed({ navigateToMain() }, 5000)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the SplashActivity
    }
}
