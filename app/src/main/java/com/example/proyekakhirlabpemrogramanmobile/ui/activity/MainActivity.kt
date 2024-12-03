package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.cdimascio.dotenv.dotenv
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cloudinary initialization
        CloudinaryManager.init(this)

        startHomeActivity() //todo

        // Firebase auth initialization
        auth = Firebase.auth

        // Binding setup
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set destination based on user auth
        binding.startButton.setOnClickListener {
            if (auth.currentUser == null) {
                startLoginActivity()
            } else {
                startHomeActivity()
            }
        }
    }

    // Move to login activity
    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // Move to home activity
    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}

object CloudinaryManager {
    private var initialized = false

    fun init(context: Context) {
        if (!initialized) {
            val dotenv = dotenv {
                directory = "/assets"
                filename = "env"
            }
            val config = hashMapOf(
                "cloud_name" to dotenv["CLOUD_NAME"],
                "secure" to true
            )
            MediaManager.init(context, config)
            initialized = true
        }
    }
}