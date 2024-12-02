package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var showPassword = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordToggle.setOnClickListener {
            if (showPassword) {
                binding.passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.password_show_icon)
            } else {
                binding.passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.password_hidden_icon)
            }
            showPassword = !showPassword
            binding.passwordInput.setSelection(binding.passwordInput.text.length)
        }

        binding.loginButton.setOnClickListener {
            val userEmail = binding.emailInput.text.toString()
            val userPassword = binding.passwordInput.text.toString()

            if (userEmail.isEmpty()) {
                showEmailError(getString(R.string.email_empty))
            } else if (userPassword.isEmpty()) {
                hideEmailError()
                showPasswordError(getString(R.string.password_empty))
            } else {
                hideEmailError()
                hidePasswordError()
                loginUser(userEmail, userPassword)
            }
        }

        binding.signupButton.setOnClickListener {
            startSignupActivity()
        }
    }

    private fun loginUser(email: String, password: String) {
        showLoading()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            hideLoading()
            if (task.isSuccessful) {
                showToast(getString(R.string.login_successful))
                startHomeActivity()
            } else {
                when (val exception = task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        showPasswordError(getString(R.string.login_error_invalid_credential))
                    }
                    else -> {
                        showPasswordError(getString(R.string.login_error_general, exception?.localizedMessage))
                    }
                }
            }
        }
    }

    private fun showEmailError(emailErrorText: String) {
        binding.emailError.visibility = View.VISIBLE
        binding.emailError.text = emailErrorText

        val params = binding.emailInput.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = 0
        binding.emailInput.layoutParams = params
    }

    private fun hideEmailError() {
        binding.emailError.visibility = View.GONE

        val params = binding.emailInput.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = convertDpToPx(20)
        binding.emailInput.layoutParams = params
    }

    private fun showPasswordError(passwordErrorText: String) {
        binding.passwordError.visibility = View.VISIBLE
        binding.passwordError.text = passwordErrorText

        val params = binding.passwordLayout.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = 0
        binding.passwordLayout.layoutParams = params
    }

    private fun hidePasswordError() {
        binding.passwordError.visibility = View.GONE

        val params = binding.passwordLayout.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = convertDpToPx(50)
        binding.passwordLayout.layoutParams = params
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.setBackgroundColor(getColor(R.color.gray))
        binding.signupButton.setBackgroundColor(getColor(R.color.gray))
        binding.loginButton.isEnabled = false
        binding.signupButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.loginButton.setBackgroundColor(getColor(R.color.pink))
        binding.signupButton.setBackgroundColor(getColor(R.color.pink))
        binding.loginButton.isEnabled = true
        binding.signupButton.isEnabled = true
    }

    private fun convertDpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun showToast(toastMessage: String) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
    }

    private fun startSignupActivity() {
        startActivity(Intent(this, SignupActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}