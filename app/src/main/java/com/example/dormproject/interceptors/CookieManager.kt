package com.example.dormproject

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

// Класс CookieManager, реализующий интерфейс CookieJar для управления куками
class CookieManager(context: Context) : CookieJar {

    // Инициализация SharedPreferences для хранения кук
    private val preferences: SharedPreferences =
        context.getSharedPreferences("cookie_prefs", Context.MODE_PRIVATE)

    // Метод saveFromResponse для сохранения кук из ответа сервера
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        // Получение редактора SharedPreferences
        val editor = preferences.edit()
        // Сохранение каждой куки в SharedPreferences
        cookies.forEach { cookie ->
            editor.putString(cookie.name, cookie.toString())
        }
        // Применение изменений
        editor.apply()
    }

    // Метод loadForRequest для загрузки кук для запроса
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // Создание списка для хранения кук
        val cookies = mutableListOf<Cookie>()
        // Загрузка всех сохраненных кук из SharedPreferences
        preferences.all.forEach { (_, value) ->
            val cookieString = value as String
            // Парсинг строки куки и добавление в список
            val cookie = Cookie.parse(url, cookieString)
            if (cookie != null) {
                cookies.add(cookie)
            }
        }
        // Возврат списка кук
        return cookies
    }

    // Метод для проверки, авторизован ли пользователь
    fun isUserLoggedIn(): Boolean {
        // Проверка наличия ключа "jwt" в SharedPreferences
        val isLoggedIn = preferences.all.keys.contains("jwt")
        return isLoggedIn
    }

    // Метод для выхода из системы (logout)
    fun logout() {
        clearCookies()
    }

    // Метод для очистки всех кук
    fun clearCookies() {
        preferences.edit().clear().apply()
    }
}