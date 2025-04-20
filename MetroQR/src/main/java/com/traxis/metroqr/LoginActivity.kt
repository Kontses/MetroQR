package com.traxis.metroqr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.traxis.metroqr.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure keyboard behavior
        binding.emailEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        binding.passwordEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        // Hide keyboard when clicking outside
        binding.root.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE)

        // Check if user is already logged in
        if (auth.currentUser != null) {
            // User is already signed in, go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // If not logged in, check for saved credentials
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        val rememberMe = sharedPreferences.getBoolean("remember_me", false)

        if (rememberMe && !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            // Auto-fill the form
            binding.emailEditText.setText(savedEmail)
            binding.passwordEditText.setText(savedPassword)
            binding.rememberMeCheckBox.isChecked = true
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.rememberMeCheckBox.isChecked) {
                // Save credentials
                sharedPreferences.edit().apply {
                    putString("email", email)
                    putString("password", password)
                    putBoolean("remember_me", true)
                    apply()
                }
            } else {
                // Clear saved credentials
                sharedPreferences.edit().clear().apply()
            }

            loginUser(email, password)
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", 
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
} 