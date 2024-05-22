package com.example.invapp.model

data class LoginRequest(
    val userName: String,
    val password: String
)

data class LoginResponse(
    val id: Int,
    val userName: String,
    val email: String,
    // Include other fields if needed
)
