

package com.example.invapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.invapp.model.Invoice
import com.example.invapp.network.ApiClient
import com.example.invapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.*

class AddInvoiceActivity : AppCompatActivity() {

    private lateinit var etClientName: EditText
    private lateinit var etAmount: EditText
    private lateinit var tvPickDate: TextView
    private lateinit var etDescription: EditText
    private lateinit var btnCreate: Button
    private var selectedDate: String = ""
    private var userId: Int = 0

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_invoice)

        etClientName = findViewById(R.id.editTextText)
        etAmount = findViewById(R.id.editTextText2)
        tvPickDate = findViewById(R.id.tv_pick_date)
        etDescription = findViewById(R.id.editTextText3)
        btnCreate = findViewById(R.id.button)

        userId = intent.getIntExtra("userId", 0)
        apiService = ApiClient.apiService

        tvPickDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnCreate.setOnClickListener {
            val clientName = etClientName.text.toString()
            val amountText = etAmount.text.toString()
            val amount = amountText.toDoubleOrNull()
            val description = etDescription.text.toString()

            if (clientName.isEmpty() || amountText.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            } else if (amount == null) {
                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_LONG).show()
            } else if (amount <= 0) {
                Toast.makeText(this, "Amount should be positive", Toast.LENGTH_LONG).show()
            } else if (amount < 3000) {
                Toast.makeText(this, "Amount should be greater than 3000", Toast.LENGTH_LONG).show()
            } else {
                val invoiceData = Invoice(
                    clientName = clientName,
                    amount = amount,
                    invoiceDate = selectedDate,
                    description = description,
                    userId = userId
                )
                createInvoice(invoiceData)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//date picke
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val month = selectedMonth + 1 // Month is 0-based in DatePickergit
            selectedDate = "$selectedYear-${month.twoDigitFormat()}-${selectedDay.twoDigitFormat()}"
            tvPickDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun Int.twoDigitFormat() = String.format("%02d", this)

    private fun createInvoice(invoiceData: Invoice) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Make the API call using Retrofit
                val response = apiService.addInvoice(invoiceData)

                // Handle the response
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!
                        val responseString = responseBody.string()
                        Toast.makeText(this@AddInvoiceActivity, responseString, Toast.LENGTH_LONG).show()
                        // Start InvoiceListActivity
                        startActivity(Intent(this@AddInvoiceActivity, InvoiceListActivity::class.java))
                        finish() // Optional: finish current activity to prevent going back
                    } else {
                        // Handle error response
                        Toast.makeText(this@AddInvoiceActivity, "Failed with response code: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: HttpException) {
                Log.e("Network_Error", "HTTP Exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("Network_Error", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
