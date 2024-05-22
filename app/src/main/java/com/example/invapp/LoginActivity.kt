package com.example.invapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.invapp.model.LoginRequest
import com.example.invapp.network.ApiClient
import com.example.invapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        apiService = ApiClient.apiService

        val etUsername = findViewById<EditText>(R.id.et_loginusername)
        val etPassword = findViewById<EditText>(R.id.et_loginpass)
        val btnLogin = findViewById<Button>(R.id.btn_log_in)
        val btnSignUp = findViewById<Button>(R.id.btn_sign_in)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this@LoginActivity, "Please enter a valid username and password", Toast.LENGTH_SHORT).show()
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.loginUser(LoginRequest(username, password))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val userId = response.body()!!.id
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                        startAddInvoiceActivity(userId)
                    } else {
                        handleLoginError(response.code(), response.message())
                    }
                }
            } catch (e: HttpException) {
                handleError("HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                handleError("Network Error: ${e.message}", e)
            }
        }
    }

    private fun handleLoginError(code: Int, message: String) {
        val errorMessage = when (code) {
            400 -> "Bad Request: Please check your input."
            401 -> "Unauthorized: Please check your credentials."
            500 -> "Server Error: Please try again later."
            else -> "Login failed: $code - $message"
        }
        Log.e("API_ERROR", errorMessage)
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleError(message: String, e: Exception) {
        Log.e("Network_Error", message, e)
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startAddInvoiceActivity(userId: Int) {
        val intent = Intent(this@LoginActivity, AddInvoiceActivity::class.java).apply {
            putExtra("userId", userId)
        }
        startActivity(intent)
    }
}
