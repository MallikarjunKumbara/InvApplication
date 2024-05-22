package com.example.invapp.network

import com.example.invapp.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val retrofit by lazy {

//        Logging Interceptor Setup
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

//        OkHttpClient Configuration
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .readTimeout(30, TimeUnit.SECONDS) // Set read timeout to 30 seconds
            .writeTimeout(30, TimeUnit.SECONDS) // Set write timeout to 30 seconds
            .build()

//        Gson Configuration
        val gson = GsonBuilder().setLenient().create()

//        Retrofit Configuration
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
//    ApiService Initialization
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
