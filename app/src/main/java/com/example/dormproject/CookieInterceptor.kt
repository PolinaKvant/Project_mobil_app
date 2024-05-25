package com.example.dormproject

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Cookie

class CookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalResponse = chain.proceed(originalRequest)

        val responseCookies: List<Cookie>?

        // Получаем все куки из ответа
        val setCookies = originalResponse.headers["Set-Cookie"]

        val cookie = setCookies?.split(";")

        if (cookie != null) {
            val parsedCookie = cookie.map { it.trim().split("=") }
            if (parsedCookie.isNotEmpty()) {
                responseCookies = parsedCookie.map { Cookie.Builder().name(it[0]).value(it[1]).domain("dorma.virusbeats.ru").build() }
                CookieJar.saveCookies(responseCookies)
            }
        }

        // Добавляем куки в заголовок следующего запроса
        val newRequest = originalRequest.newBuilder()
            .header("Cookie", CookieJar.getCookies().joinToString("; ") { it.name + "=" + it.value })
            .build()

        return chain.proceed(newRequest)
    }
}

object CookieJar {
    private var cookies: MutableList<Cookie>? = null

    fun saveCookies(cookies: List<Cookie>) {
        this.cookies = cookies as MutableList<Cookie>
    }

    fun getCookies(): List<Cookie> {
        return cookies ?: emptyList()
    }
}
