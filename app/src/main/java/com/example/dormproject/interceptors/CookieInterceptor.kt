package com.example.dormproject

import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor(private val cookieManager: CookieManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cookies = cookieManager.loadForRequest(request.url)
        val cookieHeader = cookies.joinToString("; ") { "${it.name}=${it.value}" }
        val newRequest = request.newBuilder()
            .header("Cookie", cookieHeader)
            .build()
        return chain.proceed(newRequest)
    }
}
