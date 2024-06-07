package com.example.dormproject

import okhttp3.Interceptor
import okhttp3.Response

// Класс CookieInterceptor, реализующий интерфейс Interceptor для перехвата и изменения запросов
class CookieInterceptor(private val cookieManager: CookieManager) : Interceptor {
    // Метод intercept для перехвата запроса и добавления заголовка Cookie
    override fun intercept(chain: Interceptor.Chain): Response {
        // Получение оригинального запроса
        val request = chain.request()
        // Загрузка сохраненных кук для данного URL
        val cookies = cookieManager.loadForRequest(request.url)
        // Формирование заголовка Cookie из списка кук
        val cookieHeader = cookies.joinToString("; ") { "${it.name}=${it.value}" }
        // Создание нового запроса с добавленным заголовком Cookie
        val newRequest = request.newBuilder()
            .header("Cookie", cookieHeader)
            .build()
        // Продолжение выполнения запроса с новыми заголовками
        return chain.proceed(newRequest)
    }
}