package com.example.invapp.model

data class Invoice(
    val id: Int? = null, // Make id optional if not needed at creation
    val clientName: String,
    val amount: Double,
    val invoiceDate: String,
    val description: String,
    val userId: Int
)
