package com.example.invapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.invapp.model.Invoice

class InvoiceAdapter(private val invoices: List<Invoice>) : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clientNameTextView: TextView = itemView.findViewById(R.id.clientNameTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val invoiceDateTextView: TextView = itemView.findViewById(R.id.invoiceDateTextView)
        private val editInvoiceDetails: ImageView = itemView.findViewById(R.id.imageView2)

        init {
            editInvoiceDetails.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = itemView.context
                    val intent = Intent(context, UpdateInvoiceActivity::class.java)
                    val currentInvoice = invoices[position]
                    intent.apply {
                        putExtra("INVOICE_ID", currentInvoice.id)
                        putExtra("CLIENT_NAME", currentInvoice.clientName)
                        putExtra("AMOUNT", currentInvoice.amount)
                        putExtra("INVOICE_DATE", currentInvoice.invoiceDate)
                        putExtra("DESCRIPTION", currentInvoice.description)
                    }
                    context.startActivity(intent)
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = itemView.context
                    val intent = Intent(context, ViewInvoiceActivity::class.java)
                    val currentInvoice = invoices[position]
                    intent.apply {
                        putExtra("INVOICE_ID", currentInvoice.id)
                        putExtra("CLIENT_NAME", currentInvoice.clientName)
                        putExtra("AMOUNT", currentInvoice.amount)
                        putExtra("INVOICE_DATE", currentInvoice.invoiceDate)
                        putExtra("DESCRIPTION", currentInvoice.description)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.invoice_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentInvoice = invoices[position]
        holder.clientNameTextView.text = currentInvoice.clientName
        holder.amountTextView.text = "Amount: ${currentInvoice.amount}"
        holder.invoiceDateTextView.text = "Date: ${currentInvoice.invoiceDate}"
    }

    override fun getItemCount() = invoices.size
}
