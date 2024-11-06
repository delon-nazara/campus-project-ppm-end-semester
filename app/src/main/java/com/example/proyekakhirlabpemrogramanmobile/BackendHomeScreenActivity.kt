package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BackendHomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra("EMAIL")
        val emailTextView = findViewById<TextView>(R.id.textViewEmail)
        emailTextView.text = getString(R.string.email_user, email)

        val password = intent.getStringExtra("PASSWORD")
        val passwordTextView = findViewById<TextView>(R.id.textViewPassword)
        passwordTextView.text = getString(R.string.password_user, password)

        val mainScreenButton = findViewById<Button>(R.id.buttonMainScreen)
        mainScreenButton.setOnClickListener {
            val intent = Intent(this, BackendMainScreenActivity::class.java)
            startActivity(intent)
        }

        val cameraScreenButton = findViewById<Button>(R.id.buttonCameraScreen)
        cameraScreenButton.setOnClickListener {
            val intent = Intent(this, BackendCameraScreenActivity::class.java)
            startActivity(intent)
        }
    }
}