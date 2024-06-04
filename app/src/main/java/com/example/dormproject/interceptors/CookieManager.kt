package com.example.dormproject

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager(context: Context) : CookieJar {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("cookie_prefs", Context.MODE_PRIVATE)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val editor = preferences.edit()
        cookies.forEach { cookie ->
            editor.putString(cookie.name, cookie.toString())
        }
        editor.apply()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = mutableListOf<Cookie>()
        preferences.all.forEach { (key, value) ->
            val cookieString = value as String
            val cookie = Cookie.parse(url, cookieString)
            if (cookie != null) {
                cookies.add(cookie)
            }
        }
        return cookies
    }

    fun isUserLoggedIn(): Boolean {
        val isLoggedIn = preferences.all.keys.contains("jwt")
        return isLoggedIn
    }

    fun logout() {
        clearCookies()
    }

    fun clearCookies() {
        preferences.edit().clear().apply()
    }
}
