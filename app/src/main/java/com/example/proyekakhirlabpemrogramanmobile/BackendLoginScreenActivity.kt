package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class BackendLoginScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            login(emailEditText.text.toString(), passwordEditText.text.toString())
        }

        val mainScreenButton = findViewById<Button>(R.id.buttonMainScreen)
        mainScreenButton.setOnClickListener {
            val intent = Intent(this, BackendMainScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val registerScreenButton = findViewById<Button>(R.id.buttonRegisterScreen)
        registerScreenButton.setOnClickListener {
            val intent = Intent(this, BackendRegisterScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_empty), Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.password_empty), Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, BackendHomeScreenActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                when (val exception = task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(this, getString(R.string.login_error_invalid_credential), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, getString(R.string.login_error_general, exception?.message), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}