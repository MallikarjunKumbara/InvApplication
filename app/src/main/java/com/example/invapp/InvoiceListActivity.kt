package com.example.invapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.invapp.network.ApiClient
import com.example.invapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class InvoiceListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InvoiceAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        apiService = ApiClient.apiService

        fetchInvoices()
    }

    private fun fetchInvoices() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getAllInvoices()
                if (response.isSuccessful && response.body() != null) {
                    val invoices = response.body()!!
                    withContext(Dispatchers.Main) {
                        adapter = InvoiceAdapter(invoices)
                        recyclerView.adapter = adapter
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Handle the error response
                        // Display error message to the user
//                        showToast("Failed to fetch invoices")
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    // Handle HTTP exception
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle general exception
                    e.printStackTrace()
                }
            }
        }
    }
}

























//////
//////package com.example.invapp
//////
//////import android.content.Intent
//////import android.os.Bundle
//////import android.widget.Toast
//////import androidx.appcompat.app.AppCompatActivity
//////import androidx.lifecycle.lifecycleScope
//////import androidx.recyclerview.widget.LinearLayoutManager
//////import androidx.recyclerview.widget.RecyclerView
//////import com.example.invapp.InvoiceAdapter
//////import com.example.invapp.model.Invoice
//////import com.example.invapp.network.ApiClient
//////import com.example.invapp.network.ApiService
//////import kotlinx.coroutines.Dispatchers
//////import kotlinx.coroutines.launch
//////import kotlinx.coroutines.withContext
//////import retrofit2.HttpException
//////
//////class InvoiceListActivity : AppCompatActivity(), InvoiceAdapter.OnItemClickListener {
//////
//////    private lateinit var recyclerView: RecyclerView
//////    private lateinit var adapter: InvoiceAdapter
//////    private lateinit var apiService: ApiService
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
//////        setContentView(R.layout.activity_invoice_list)
//////
//////        recyclerView = findViewById(R.id.recyclerView)
//////        recyclerView.layoutManager = LinearLayoutManager(this)
//////        adapter = InvoiceAdapter(emptyList(), this)
//////        recyclerView.adapter = adapter
//////
//////        apiService = ApiClient.apiService
//////
//////        fetchInvoices()
//////    }
//////
//////    private fun fetchInvoices() {
//////        lifecycleScope.launch(Dispatchers.IO) {
//////            try {
//////                val response = apiService.getAllInvoices()
//////                if (response.isSuccessful && response.body() != null) {
//////                    val invoices = response.body()!!
//////                    withContext(Dispatchers.Main) {
//////                        adapter.updateInvoices(invoices)
//////                    }
//////                } else {
//////                    withContext(Dispatchers.Main) {
//////                        showToast("Failed to fetch invoices")
//////                    }
//////                }
//////            } catch (e: HttpException) {
//////                withContext(Dispatchers.Main) {
//////                    showToast("HttpException: ${e.message()}")
//////                }
//////            } catch (e: Exception) {
//////                withContext(Dispatchers.Main) {
//////                    showToast("Exception: ${e.message}")
//////                }
//////            }
//////        }
//////    }
//////
//////    private fun showToast(message: String) {
//////        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//////    }
//////
//////    override fun onItemClick(invoice: Invoice) {
//////        val intent = Intent(this, ViewInvoiceActivity::class.java)
//////        intent.putExtra("INVOICE_ID", invoice.id)
//////        intent.putExtra("CLIENT_NAME", invoice.clientName)
//////        intent.putExtra("AMOUNT", invoice.amount)
//////        intent.putExtra("INVOICE_DATE", invoice.invoiceDate)
//////        intent.putExtra("DESCRIPTION", invoice.description)
//////        startActivity(intent)
//////    }
//////}
//package com.example.invapp
//import InvoiceAdapter
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.invapp.R
//import com.example.invapp.ViewInvoiceActivity
//import com.example.invapp.model.Invoice
//import com.example.invapp.network.ApiClient
//import com.example.invapp.network.ApiService
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//
//class InvoiceListActivity : AppCompatActivity(), InvoiceAdapter.OnItemClickListener {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: InvoiceAdapter
//    private lateinit var apiService: ApiService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_invoice_list)
//
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        apiService = ApiClient.apiService
//
//        adapter = InvoiceAdapter(mutableListOf(), this)
//        recyclerView.adapter = adapter
//
//        fetchInvoices()
//    }
//
//    private fun fetchInvoices() {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val response = apiService.getAllInvoices()
//                if (response.isSuccessful && response.body() != null) {
//                    val invoices = response.body()!!
//                    withContext(Dispatchers.Main) {
//                        adapter.updateInvoices(invoices)
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        // Handle the error response
//                        // Display error message to the user
//                    }
//                }
//            } catch (e: HttpException) {
//                withContext(Dispatchers.Main) {
//                    // Handle HTTP exception
//                    e.printStackTrace()
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    // Handle general exception
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    override fun onItemClick(invoice: Invoice) {
//        val intent = Intent(this, ViewInvoiceActivity::class.java)
//        intent.putExtra("INVOICE_ID", invoice.id)
//        intent.putExtra("CLIENT_NAME", invoice.clientName)
//        intent.putExtra("AMOUNT", invoice.amount)
//        intent.putExtra("INVOICE_DATE", invoice.invoiceDate)
//        intent.putExtra("DESCRIPTION", invoice.description)
//        startActivity(intent)
//    }
//}
////package com.example.invapp
////import InvoiceAdapter
////import android.content.Intent
////import android.os.Bundle
////import androidx.appcompat.app.AppCompatActivity
////import androidx.recyclerview.widget.LinearLayoutManager
////import androidx.recyclerview.widget.RecyclerView
////import com.example.invapp.model.Invoice
////import com.example.invapp.R
////
////class InvoiceListActivity : AppCompatActivity(), InvoiceAdapter.OnItemClickListener {
////
////    private lateinit var recyclerView: RecyclerView
////    private lateinit var adapter: InvoiceAdapter
////    private val invoices = mutableListOf<Invoice>() // Sample list of invoices
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_invoice_list)
////
////        recyclerView = findViewById(R.id.recyclerView)
////        recyclerView.layoutManager = LinearLayoutManager(this)
////        adapter = InvoiceAdapter(invoices, this)
////        recyclerView.adapter = adapter
////
////
////    }
////
////    override fun onItemClick(invoice: Invoice) {
////        val intent = Intent(this, ViewInvoiceActivity::class.java)
////        intent.putExtra("INVOICE_ID", invoice.id)
////        intent.putExtra("CLIENT_NAME", invoice.clientName)
////        intent.putExtra("AMOUNT", invoice.amount)
////        intent.putExtra("INVOICE_DATE", invoice.invoiceDate)
////        intent.putExtra("DESCRIPTION", invoice.description)
////        startActivity(intent)
////    }
////}
