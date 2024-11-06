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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class BackendRegisterScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_register_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            register(emailEditText.text.toString(), passwordEditText.text.toString())
        }

        val mainScreenButton = findViewById<Button>(R.id.buttonMainScreen)
        mainScreenButton.setOnClickListener {
            val intent = Intent(this, BackendMainActivity::class.java)
            startActivity(intent)
        }

        val loginScreenButton = findViewById<Button>(R.id.buttonLoginScreen)
        loginScreenButton.setOnClickListener {
            val intent = Intent(this, BackendLoginScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(email: String, password: String) {

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_empty), Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.password_empty), Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, getString(R.string.register_successful), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, BackendHomeScreenActivity::class.java)
                intent.putExtra("EMAIL", email)
                intent.putExtra("PASSWORD", password)
                startActivity(intent)
            } else {
                when (val exception = task.exception) {
                    is FirebaseAuthUserCollisionException -> {
                        Toast.makeText(this, getString(R.string.register_error_email_registered), Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseAuthWeakPasswordException -> {
                        Toast.makeText(this, getString(R.string.register_error_short_password), Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(this, getString(R.string.register_error_invalid_credential), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}