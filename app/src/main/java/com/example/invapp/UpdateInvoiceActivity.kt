
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

class UpdateInvoiceActivity : AppCompatActivity() {

    private lateinit var etClientName: EditText
    private lateinit var etAmount: EditText
    private lateinit var tvPickDate: TextView
    private lateinit var etDescription: EditText
    private lateinit var btnUpdate: Button
    private var selectedDate: String = ""
    private var userId: Int = 0
    private var invoiceId: Int = 0

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_invoice)

        etClientName = findViewById(R.id.editTextText)
        etAmount = findViewById(R.id.editTextText2)
        tvPickDate = findViewById(R.id.tv_pick_date)
        etDescription = findViewById(R.id.editTextText3)
        btnUpdate = findViewById(R.id.editbutton)

        apiService = ApiClient.apiService

        // Get invoice details from intent
        intent?.let {
            invoiceId = it.getIntExtra("INVOICE_ID", 0)
            etClientName.setText(it.getStringExtra("CLIENT_NAME"))
            etAmount.setText(it.getDoubleExtra("AMOUNT", 0.0).toString())
            selectedDate = it.getStringExtra("INVOICE_DATE").orEmpty()
            tvPickDate.text = selectedDate
            etDescription.setText(it.getStringExtra("DESCRIPTION"))
            userId = it.getIntExtra("USER_ID", 0)
        }

        tvPickDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnUpdate.setOnClickListener {
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
                    id = invoiceId,
                    clientName = clientName,
                    amount = amount,
                    invoiceDate = selectedDate,
                    description = description,
                    userId = userId
                )
                updateInvoice(invoiceId, invoiceData)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val month = selectedMonth + 1 // Month is 0-based in DatePicker
            selectedDate = "$selectedYear-${month.twoDigitFormat()}-${selectedDay.twoDigitFormat()}"
            tvPickDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun Int.twoDigitFormat() = String.format("%02d", this)

    private fun updateInvoice(id: Int, invoiceData: Invoice) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Make the API call using Retrofit
                val response = apiService.updateInvoice(id, invoiceData)

                // Handle the response
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UpdateInvoiceActivity, "Invoice updated successfully", Toast.LENGTH_LONG).show()
                        // Start InvoiceListActivity
                        startActivity(Intent(this@UpdateInvoiceActivity, InvoiceListActivity::class.java))
                        finish() // Optional: finish current activity to prevent going back
                    } else {
                        // Handle error response
                        Toast.makeText(this@UpdateInvoiceActivity, "Failed with response code: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: HttpException) {
                Log.e("Network_Error", "HTTP Exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("Network_Error", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}















//////package com.example.invapp
//////
//////import android.app.DatePickerDialog
//////import android.content.Intent
//////import android.os.Bundle
//////import android.util.Log
//////import android.widget.Button
//////import android.widget.EditText
//////import android.widget.TextView
//////import android.widget.Toast
//////import androidx.appcompat.app.AppCompatActivity
//////import com.example.invapp.network.ApiClient
//////import kotlinx.coroutines.*
//////import org.json.JSONObject
//////import java.util.*
//////
//////class UpdateInvoiceActivity : AppCompatActivity() {
//////
//////    private lateinit var etClientName: EditText
//////    private lateinit var etAmount: EditText
//////    private lateinit var tvPickDate: TextView
//////    private lateinit var etDescription: EditText
//////    private lateinit var btnUpdate: Button
//////    private var selectedDate: String = ""
//////    private var invoiceId: Int = 0
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
//////        setContentView(R.layout.activity_update_invoice)
//////
//////        etClientName = findViewById(R.id.editTextText)
//////        etAmount = findViewById(R.id.editTextText2)
//////        tvPickDate = findViewById(R.id.tv_pick_date)
//////        etDescription = findViewById(R.id.editTextText3)
//////        btnUpdate = findViewById(R.id.editbutton)
//////
//////        // Get the invoice details from the intent
//////        val intent = intent
//////        invoiceId = intent.getIntExtra("INVOICE_ID", 0)
//////        val clientName = intent.getStringExtra("CLIENT_NAME") ?: ""
//////        val amount = intent.getDoubleExtra("AMOUNT", 0.0)
//////        val invoiceDate = intent.getStringExtra("INVOICE_DATE") ?: ""
//////        val description = intent.getStringExtra("DESCRIPTION") ?: ""
//////
//////        // Set the fields with the received data
//////        etClientName.setText(clientName)
//////        etAmount.setText(amount.toString())
//////        tvPickDate.text = invoiceDate
//////        etDescription.setText(description)
//////        selectedDate = invoiceDate
//////
//////        tvPickDate.setOnClickListener {
//////            showDatePickerDialog()
//////        }
//////
//////        btnUpdate.setOnClickListener {
//////            val updatedClientName = etClientName.text.toString()
//////            val updatedAmountText = etAmount.text.toString()
//////            val updatedAmount = updatedAmountText.toDoubleOrNull()
//////            val updatedDescription = etDescription.text.toString()
//////
//////            if (updatedClientName.isEmpty() || updatedAmountText.isEmpty() || selectedDate.isEmpty() || updatedDescription.isEmpty()) {
//////                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
//////            } else if (updatedAmount == null) {
//////                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_LONG).show()
//////            } else if (updatedAmount <= 0) {
//////                Toast.makeText(this, "Amount should be positive", Toast.LENGTH_LONG).show()
//////            } else if (updatedAmount < 3000) {
//////                Toast.makeText(this, "Amount should be greater than 3000", Toast.LENGTH_LONG).show()
//////            } else {
//////                val invoiceData = JSONObject().apply {
//////                    put("clientName", updatedClientName)
//////                    put("amount", updatedAmount)
//////                    put("invoiceDate", selectedDate)
//////                    put("description", updatedDescription)
//////                }
//////                updateInvoice(invoiceId, invoiceData)
//////            }
//////        }
//////    }
//////
//////    private fun showDatePickerDialog() {
//////        val calendar = Calendar.getInstance()
//////        val year = calendar.get(Calendar.YEAR)
//////        val month = calendar.get(Calendar.MONTH)
//////        val day = calendar.get(Calendar.DAY_OF_MONTH)
//////
//////        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
//////            val monthFormatted = (selectedMonth + 1).twoDigitFormat()
//////            val dayFormatted = selectedDay.twoDigitFormat()
//////            selectedDate = "$selectedYear-$monthFormatted-$dayFormatted"
//////            tvPickDate.text = selectedDate
//////        }, year, month, day)
//////
//////        datePickerDialog.show()
//////    }
//////
//////    private fun Int.twoDigitFormat() = String.format("%02d", this)
//////
//////    private fun updateInvoice(invoiceId: Int, invoiceData: JSONObject) {
//////        CoroutineScope(Dispatchers.IO).launch {
//////            try {
//////                val response = ApiClient.apiService.updateInvoice(invoiceId, invoiceData.toString())
//////                withContext(Dispatchers.Main) {
//////                    if (response.isSuccessful) {
//////                        Toast.makeText(this@UpdateInvoiceActivity, "Invoice updated successfully", Toast.LENGTH_LONG).show()
//////                        val intent = Intent(this@UpdateInvoiceActivity, InvoiceListActivity::class.java)
//////                        startActivity(intent)
//////                        finish()
//////                    } else {
//////                        Toast.makeText(this@UpdateInvoiceActivity, "Failed with response code: ${response.code()}", Toast.LENGTH_LONG).show()
//////                    }
//////                }
//////            } catch (e: Exception) {
//////                withContext(Dispatchers.Main) {
//////                    Toast.makeText(this@UpdateInvoiceActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
//////                }
//////                Log.e("UpdateInvoice", "Exception: ${e.message}", e)
//////            }
//////        }
//////    }
//////}
//////package com.example.invapp
//////
//////import android.app.DatePickerDialog
//////import android.content.Intent
//////import android.os.Bundle
//////import android.util.Log
//////import android.widget.Button
//////import android.widget.EditText
//////import android.widget.TextView
//////import android.widget.Toast
//////import androidx.appcompat.app.AppCompatActivity
//////import androidx.lifecycle.lifecycleScope
//////import com.example.invapp.model.Invoice
//////import com.example.invapp.network.ApiClient
//////import com.example.invapp.network.ApiService
//////import kotlinx.coroutines.Dispatchers
//////import kotlinx.coroutines.launch
//////import kotlinx.coroutines.withContext
//////import retrofit2.HttpException
//////import java.util.*
//////
//////class UpdateInvoiceActivity : AppCompatActivity() {
//////
//////    private lateinit var etClientName: EditText
//////    private lateinit var etAmount: EditText
//////    private lateinit var tvPickDate: TextView
//////    private lateinit var etDescription: EditText
//////    private lateinit var btnUpdate: Button
//////    private var selectedDate: String = ""
//////    private var userId: Int = 0
//////    private var invoiceId: Int = 0
//////
//////    private lateinit var apiService: ApiService
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
//////        setContentView(R.layout.activity_update_invoice)
//////
//////        etClientName = findViewById(R.id.editTextText)
//////        etAmount = findViewById(R.id.editTextText2)
//////        tvPickDate = findViewById(R.id.tv_pick_date)
//////        etDescription = findViewById(R.id.editTextText3)
//////        btnUpdate = findViewById(R.id.editbutton)
//////
//////        userId = intent.getIntExtra("userId", 0)
//////        invoiceId = intent.getIntExtra("invoiceId", 0)
//////        apiService = ApiClient.apiService
//////
//////        tvPickDate.setOnClickListener {
//////            showDatePickerDialog()
//////        }
//////
//////        btnUpdate.setOnClickListener {
//////            val clientName = etClientName.text.toString()
//////            val amountText = etAmount.text.toString()
//////            val amount = amountText.toDoubleOrNull()
//////            val description = etDescription.text.toString()
//////
//////            if (clientName.isEmpty() || amountText.isEmpty() || description.isEmpty()) {
//////                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
//////            } else if (amount == null) {
//////                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_LONG).show()
//////            } else if (amount <= 0) {
//////                Toast.makeText(this, "Amount should be positive", Toast.LENGTH_LONG).show()
//////            } else if (amount < 3000) {
//////                Toast.makeText(this, "Amount should be greater than 3000", Toast.LENGTH_LONG).show()
//////            } else {
//////                val invoiceData = Invoice(
//////                    clientName = clientName,
//////                    amount = amount,
//////                    invoiceDate = selectedDate,
//////                    description = description,
//////                    userId = userId
//////                )
//////                updateInvoice(invoiceId, invoiceData)
//////            }
//////        }
//////    }
//////
//////    private fun showDatePickerDialog() {
//////        val calendar = Calendar.getInstance()
//////        val year = calendar.get(Calendar.YEAR)
//////        val month = calendar.get(Calendar.MONTH)
//////        val day = calendar.get(Calendar.DAY_OF_MONTH)
//////
//////        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
//////            val month = selectedMonth + 1 // Month is 0-based in DatePicker
//////            selectedDate = "$selectedYear-${month.twoDigitFormat()}-${selectedDay.twoDigitFormat()}"
//////            tvPickDate.text = selectedDate
//////        }, year, month, day)
//////
//////        datePickerDialog.show()
//////    }
//////
//////    private fun Int.twoDigitFormat() = String.format("%02d", this)
//////
//////    private fun updateInvoice(id: Int, invoiceData: Invoice) {
//////        lifecycleScope.launch(Dispatchers.IO) {
//////            try {
//////                // Make the API call using Retrofit
//////                val response = apiService.updateInvoice(id, invoiceData)
//////
//////                // Handle the response
//////                withContext(Dispatchers.Main) {
//////                    if (response.isSuccessful) {
//////                        Toast.makeText(this@UpdateInvoiceActivity, "Invoice updated successfully", Toast.LENGTH_LONG).show()
//////                        // Start InvoiceListActivity
//////                        startActivity(Intent(this@UpdateInvoiceActivity, InvoiceListActivity::class.java))
//////                        finish() // Optional: finish current activity to prevent going back
//////                    } else {
//////                        // Handle error response
//////                        Toast.makeText(this@UpdateInvoiceActivity, "Failed with response code: ${response.code()}", Toast.LENGTH_LONG).show()
//////                    }
//////                }
//////            } catch (e: HttpException) {
//////                Log.e("Network_Error", "HTTP Exception: ${e.message}", e)
//////                withContext(Dispatchers.Main) {
//////                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
//////                }
//////            } catch (e: Exception) {
//////                Log.e("Network_Error", "Error: ${e.message}", e)
//////                withContext(Dispatchers.Main) {
//////                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
//////                }
//////            }
//////        }
//////    }
//////}
////// UpdateInvoiceActivity.kt
////package com.example.invapp
////
////import android.app.DatePickerDialog
////import android.content.Intent
////import android.os.Bundle
////import android.util.Log
////import android.widget.Button
////import android.widget.EditText
////import android.widget.TextView
////import android.widget.Toast
////import androidx.appcompat.app.AppCompatActivity
////import androidx.lifecycle.lifecycleScope
////import com.example.invapp.model.Invoice
////import com.example.invapp.network.ApiClient
////import com.example.invapp.network.ApiService
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.withContext
////import retrofit2.HttpException
////import java.util.*
////
////class UpdateInvoiceActivity : AppCompatActivity() {
////
////    private lateinit var etClientName: EditText
////    private lateinit var etAmount: EditText
////    private lateinit var tvPickDate: TextView
////    private lateinit var etDescription: EditText
////    private lateinit var btnUpdate: Button
////    private var selectedDate: String = ""
////    private var userId: Int = 0
////    private var invoiceId: Int = 0
////
////    private lateinit var apiService: ApiService
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_update_invoice)
////
////        etClientName = findViewById(R.id.editTextText)
////        etAmount = findViewById(R.id.editTextText2)
////        tvPickDate = findViewById(R.id.tv_pick_date)
////        etDescription = findViewById(R.id.editTextText3)
////        btnUpdate = findViewById(R.id.editbutton)
////
////        apiService = ApiClient.apiService
////
////        // Get invoice details from intent
////        intent?.let {
////            invoiceId = it.getIntExtra("INVOICE_ID", 0)
////            etClientName.setText(it.getStringExtra("CLIENT_NAME"))
////            etAmount.setText(it.getDoubleExtra("AMOUNT", 0.0).toString())
////            selectedDate = it.getStringExtra("INVOICE_DATE").orEmpty()
////            tvPickDate.text = selectedDate
////            etDescription.setText(it.getStringExtra("DESCRIPTION"))
////            userId = it.getIntExtra("USER_ID", 0)
////        }
////
////        tvPickDate.setOnClickListener {
////            showDatePickerDialog()
////        }
////
////        btnUpdate.setOnClickListener {
////            val clientName = etClientName.text.toString()
////            val amountText = etAmount.text.toString()
////            val amount = amountText.toDoubleOrNull()
////            val description = etDescription.text.toString()
////
////            if (clientName.isEmpty() || amountText.isEmpty() || description.isEmpty()) {
////                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
////            } else if (amount == null) {
////                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_LONG).show()
////            } else if (amount <= 0) {
////                Toast.makeText(this, "Amount should be positive", Toast.LENGTH_LONG).show()
////            } else if (amount < 3000) {
////                Toast.makeText(this, "Amount should be greater than 3000", Toast.LENGTH_LONG).show()
////            } else {
////                val invoiceData = Invoice(
////                    clientName = clientName,
////                    amount = amount,
////                    invoiceDate = selectedDate,
////                    description = description,
////                    userId = userId
////                )
////                updateInvoice(invoiceId, invoiceData)
////            }
////        }
////    }
////
////    private fun showDatePickerDialog() {
////        val calendar = Calendar.getInstance()
////        val year = calendar.get(Calendar.YEAR)
////        val month = calendar.get(Calendar.MONTH)
////        val day = calendar.get(Calendar.DAY_OF_MONTH)
////
////        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
////            val month = selectedMonth + 1 // Month is 0-based in DatePicker
////            selectedDate = "$selectedYear-${month.twoDigitFormat()}-${selectedDay.twoDigitFormat()}"
////            tvPickDate.text = selectedDate
////        }, year, month, day)
////
////        datePickerDialog.show()
////    }
////
////    private fun Int.twoDigitFormat() = String.format("%02d", this)
////
////    private fun updateInvoice(id: Int, invoiceData: Invoice) {
////        lifecycleScope.launch(Dispatchers.IO) {
////            try {
////                // Make the API call using Retrofit
////                val response = apiService.updateInvoice(id, invoiceData)
////
////                // Handle the response
////                withContext(Dispatchers.Main) {
////                    if (response.isSuccessful) {
////                        Toast.makeText(this@UpdateInvoiceActivity, "Invoice updated successfully", Toast.LENGTH_LONG).show()
////                        // Start InvoiceListActivity
////                        startActivity(Intent(this@UpdateInvoiceActivity, InvoiceListActivity::class.java))
////                        finish() // Optional: finish current activity to prevent going back
////                    } else {
////                        // Handle error response
////                        Toast.makeText(this@UpdateInvoiceActivity, "Failed with response code: ${response.code()}", Toast.LENGTH_LONG).show()
////                    }
////                }
////            } catch (e: HttpException) {
////                Log.e("Network_Error", "HTTP Exception: ${e.message}", e)
////                withContext(Dispatchers.Main) {
////                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
////                }
////            } catch (e: Exception) {
////                Log.e("Network_Error", "Error: ${e.message}", e)
////                withContext(Dispatchers.Main) {
////                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
////                }
////            }
////        }
////    }
////}
//// UpdateInvoiceActivity.kt
//package com.example.invapp
//
//
//import android.app.DatePickerDialog
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.invapp.model.Invoice
//import com.example.invapp.network.ApiClient
//import com.example.invapp.network.ApiService
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//import java.util.*
//
//class UpdateInvoiceActivity : AppCompatActivity() {
//
//    private lateinit var etClientName: EditText
//    private lateinit var etAmount: EditText
//    private lateinit var tvPickDate: TextView
//    private lateinit var etDescription: EditText
//    private lateinit var btnUpdate: Button
//    private var selectedDate: String = ""
//    private var userId: Int = 0
//    private var invoiceId: Int = 0
//
//    private lateinit var apiService: ApiService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_update_invoice)
//
//        etClientName = findViewById(R.id.editTextText)
//        etAmount = findViewById(R.id.editTextText2)
//        tvPickDate = findViewById(R.id.tv_pick_date)
//        etDescription = findViewById(R.id.editTextText3)
//        btnUpdate = findViewById(R.id.editbutton)
//
//        apiService = ApiClient.apiService
//
//        // Get invoice details from intent
//        intent?.let {
//            invoiceId = it.getIntExtra("INVOICE_ID", 0)
//            etClientName.setText(it.getStringExtra("CLIENT_NAME"))
//            etAmount.setText(it.getDoubleExtra("AMOUNT", 0.0).toString())
//            selectedDate = it.getStringExtra("INVOICE_DATE").orEmpty()
//            tvPickDate.text = selectedDate
//            etDescription.setText(it.getStringExtra("DESCRIPTION"))
//            userId = it.getIntExtra("USER_ID", 0)
//        }
//
//        tvPickDate.setOnClickListener {
//            showDatePickerDialog()
//        }
//
//        btnUpdate.setOnClickListener {
//            val clientName = etClientName.text.toString()
//            val amountText = etAmount.text.toString()
//            val amount = amountText.toDoubleOrNull()
//            val description = etDescription.text.toString()
//
//            if (clientName.isEmpty() || amountText.isEmpty() || description.isEmpty()) {
//                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
//            } else if (amount == null) {
//                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_LONG).show()
//            } else if (amount <= 0) {
//                Toast.makeText(this, "Amount should be positive", Toast.LENGTH_LONG).show()
//            } else if (amount < 3000) {
//                Toast.makeText(this, "Amount should be greater than 3000", Toast.LENGTH_LONG).show()
//            } else {
//                val invoiceData = Invoice(
//                    id = invoiceId,
//                    clientName = clientName,
//                    amount = amount,
//                    invoiceDate = selectedDate,
//                    description = description,
//                    userId = userId
//                )
//                updateInvoice(invoiceId, invoiceData)
//            }
//        }
//    }
//
//    private fun showDatePickerDialog() {
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
//            val month = selectedMonth + 1 // Month is 0-based in DatePicker
//            selectedDate = "$selectedYear-${month.twoDigitFormat()}-${selectedDay.twoDigitFormat()}"
//            tvPickDate.text = selectedDate
//        }, year, month, day)
//
//        datePickerDialog.show()
//    }
//
//    private fun Int.twoDigitFormat() = String.format("%02d", this)
//
//    private fun updateInvoice(id: Int, invoiceData: Invoice) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                // Make the API call using Retrofit
//                val response = apiService.updateInvoice(id, invoiceData)
//
//                // Handle the response
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        Toast.makeText(this@UpdateInvoiceActivity, "Invoice updated successfully", Toast.LENGTH_LONG).show()
//                        // Start InvoiceListActivity
//                        startActivity(Intent(this@UpdateInvoiceActivity, InvoiceListActivity::class.java))
//                        finish() // Optional: finish current activity to prevent going back
//                    } else {
//                        // Handle error response
//                        Toast.makeText(this@UpdateInvoiceActivity, "Failed with response code: ${response.code()}", Toast.LENGTH_LONG).show()
//                    }
//                }
//            } catch (e: HttpException) {
//                Log.e("Network_Error", "HTTP Exception: ${e.message}", e)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            } catch (e: Exception) {
//                Log.e("Network_Error", "Error: ${e.message}", e)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@UpdateInvoiceActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//}
// UpdateInvoiceActivity.kt