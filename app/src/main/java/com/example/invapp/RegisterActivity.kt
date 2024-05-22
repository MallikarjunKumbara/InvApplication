package com.example.invapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.invapp.network.ApiClient
import com.example.invapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        apiService = ApiClient.apiService

        val etUsername = findViewById<EditText>(R.id.et_username)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (isValidPassword(password)) {
                // Log the username, email, and password before making the request
                Log.d("REGISTER_INPUT", "Username: $username, Email: $email, Password: $password")
                registerUser(username, email, password)
            } else {
                Toast.makeText(this@RegisterActivity, "Password should contain at least 8 characters, one lowercase letter, one uppercase letter, and one special character", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()
        return passwordRegex.matches(password)
    }

    private fun registerUser(username: String, email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.registerUser(mapOf("userName" to username, "email" to email, "password" to password))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    } else {
                        val errorMessage = "Registration failed: ${response.code()} - ${response.message()}"
                        Log.e("API_ERROR", errorMessage)
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("Network_Error", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
