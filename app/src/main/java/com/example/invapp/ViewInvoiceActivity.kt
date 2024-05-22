package com.example.invapp
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.invapp.R
import com.example.invapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewInvoiceActivity : AppCompatActivity() {
    private var invoiceId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_invoice)

        // Get the invoice ID from the intent
        invoiceId = intent.getIntExtra("INVOICE_ID", 0)

        // Call deleteInvoice when the delete button is clicked
        deleteInvoice(invoiceId)
    }

    private fun deleteInvoice(invoiceId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.deleteInvoice(invoiceId)
                if (response.isSuccessful) {
                    showToast("Invoice deleted successfully")
                    finish()
                } else {
                    showToast("Failed to delete invoice: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("Exception: ${e.message}")
                Log.e("DeleteInvoice", "Exception: ${e.message}", e)
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@ViewInvoiceActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}













//package com.example.invapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.invapp.network.ApiClient
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class ViewInvoiceActivity : AppCompatActivity() {
//
//    private lateinit var clientNameTextView: TextView
//    private lateinit var amountTextView: TextView
//    private lateinit var dateTextView: TextView
//    private lateinit var descriptionTextView: TextView
//    private lateinit var btnDelete: Button
//    private var invoiceId: Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_view_invoice)
//
//        clientNameTextView = findViewById(R.id.clientNameTextView)
//        amountTextView = findViewById(R.id.amountTextView)
//        dateTextView = findViewById(R.id.dateTextView)
//        descriptionTextView = findViewById(R.id.descriptionTextView)
//        btnDelete = findViewById(R.id.deleteButton)
//
//        // Get the invoice details from the intent
//        val intent = intent
//        invoiceId = intent.getIntExtra("INVOICE_ID", 0)
//        val clientName = intent.getStringExtra("CLIENT_NAME") ?: ""
//        val amount = intent.getDoubleExtra("AMOUNT", 0.0)
//        val invoiceDate = intent.getStringExtra("INVOICE_DATE") ?: ""
//        val description = intent.getStringExtra("DESCRIPTION") ?: ""
//
//        // Set the fields with the received data
//        clientNameTextView.text = clientName
//        amountTextView.text = amount.toString()
//        dateTextView.text = invoiceDate
//        descriptionTextView.text = description
//
//        btnDelete.setOnClickListener {
//            deleteInvoice(invoiceId)
//        }
//    }
//
//    private fun deleteInvoice(invoiceId: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = ApiClient.apiService.deleteInvoice(invoiceId)
//                if (response.isSuccessful) {
//                    val message = response.body()?.toString() ?: "Invoice deleted successfully"
//                    showToast(message)
//                    finish()
//                } else {
//                    showToast("Failed to delete invoice: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                showToast("Exception: ${e.message}")
//                Log.e("DeleteInvoice", "Exception: ${e.message}", e)
//            }
//        }
//    }
//
//    private fun showToast(message: String) {
//        runOnUiThread {
//            Toast.makeText(this@ViewInvoiceActivity, message, Toast.LENGTH_LONG).show()
//        }
//    }
//}