package com.example.dormproject

import android.content.Context
import com.example.dormproject.interceptors.ResponseCheckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Объект ApiService для настройки и создания экземпляров API-сервисов
object ApiService {

    // Базовый URL для API-запросов
    private const val BASE_URL = "https://dorma.virusbeats.ru/api/"

    // Менеджер для работы с куками
    private lateinit var cookieManager: CookieManager

    // Метод для инициализации CookieManager с контекстом приложения
    fun initialize(context: Context) {
        cookieManager = CookieManager(context)
    }

    // Ленивая инициализация логирующего интерсептора для HTTP-запросов
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Ленивая инициализация OkHttpClient с добавленными интерсепторами
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ResponseCheckInterceptor())
            .addInterceptor(CookieInterceptor(cookieManager))
            .addInterceptor(loggingInterceptor)
            .cookieJar(cookieManager)
            .build()
    }

    // Ленивая инициализация Retrofit с базовым URL и клиентом
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Метод для создания экземпляра API-сервиса
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    // Метод для проверки, авторизован ли пользователь
    fun isUserLoggedIn(): Boolean {
        return cookieManager.isUserLoggedIn()
    }

    // Метод для выхода из аккаунта пользователя
    fun logout() {
        cookieManager.logout()
    }
}