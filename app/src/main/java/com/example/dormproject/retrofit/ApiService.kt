package com.example.dormproject

import android.content.Context
import com.example.dormproject.interceptors.ResponseCheckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "https://dorma.virusbeats.ru/api/"

    private lateinit var cookieManager: CookieManager

    fun initialize(context: Context) {
        cookieManager = CookieManager(context)
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ResponseCheckInterceptor())
            .addInterceptor(CookieInterceptor(cookieManager))
            .addInterceptor(loggingInterceptor)
            .cookieJar(cookieManager)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    fun isUserLoggedIn(): Boolean {
        return cookieManager.isUserLoggedIn()
    }
}
