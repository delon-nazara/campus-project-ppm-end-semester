package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BackendMainScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_main_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginScreenButton = findViewById<Button>(R.id.buttonLoginScreen)
        loginScreenButton.setOnClickListener {
            val intent = Intent(this, BackendLoginScreenActivity::class.java)
            startActivity(intent)
        }

        val registerScreenButton = findViewById<Button>(R.id.buttonRegisterScreen)
        registerScreenButton.setOnClickListener {
            val intent = Intent(this, BackendRegisterScreenActivity::class.java)
            startActivity(intent)
        }
    }
}