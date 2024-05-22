package com.example.invapp.network

import com.example.invapp.model.Invoice
import com.example.invapp.model.LoginRequest
import com.example.invapp.model.LoginResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE

import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("users/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>


    @POST("users/register")
    suspend fun registerUser(@Body user: Map<String, String>): Response<Void>




    @POST("invoices/create")
    suspend fun addInvoice(@Body invoice: Invoice): Response<ResponseBody>

    @GET("invoices/all")
    suspend fun getAllInvoices(): Response<List<Invoice>>


    @PUT("invoices/{id}")
    suspend fun updateInvoice(@Path("id") id: Int, @Body invoice: Invoice): Response<ResponseBody>

    @DELETE("invoices/{id}")
    suspend fun deleteInvoice(@Path("id") id: Int): Response<Unit>
}
