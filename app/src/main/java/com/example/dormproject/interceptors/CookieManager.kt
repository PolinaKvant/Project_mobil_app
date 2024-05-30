package com.example.dormproject

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import android.util.Log

class CookieManager(context: Context) : CookieJar {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("cookie_prefs", Context.MODE_PRIVATE)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val editor = preferences.edit()
        cookies.forEach { cookie ->
            editor.putString(cookie.name, cookie.toString())
            Log.d("CookieManager", "Saved cookie: ${cookie.name}=${cookie.value}")
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
                Log.d("CookieManager", "Loaded cookie: $key=${cookie.value}")
            }
        }
        return cookies
    }

    fun isUserLoggedIn(): Boolean {
        val isLoggedIn = preferences.all.keys.contains("jwt")
        Log.d("CookieManager", "Is user logged in: $isLoggedIn")
        return isLoggedIn
    }

    fun clearCookies() {
        preferences.edit().clear().apply()
        Log.d("CookieManager", "Cleared cookies")
    }
}
