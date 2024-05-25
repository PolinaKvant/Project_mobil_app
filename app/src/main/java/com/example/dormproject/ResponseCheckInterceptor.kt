package com.example.dormproject

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ResponseCheckInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            throw IOException("${response.code}")
        }
        return response
    }
}