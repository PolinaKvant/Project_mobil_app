package com.example.dormproject.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

// Класс ResponseCheckInterceptor реализует интерфейс Interceptor для проверки ответа сервера
class ResponseCheckInterceptor : Interceptor {

    // Переопределение метода intercept для перехвата запросов и ответов
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Выполнение исходного запроса
        val response = chain.proceed(chain.request())

        // Проверка, был ли успешным ответ сервера
        if (!response.isSuccessful) {
            // Если ответ не успешный, выбрасываем исключение с кодом ошибки
            throw IOException("${response.code}")
        }

        // Дополнительная проверка кода ответа (если код не равен 200)
        if(response.code != 200) {
            // Если код ответа не равен 200, выбрасываем исключение с кодом ошибки
            throw IOException("${response.code}")
        }

        // Возвращение ответа, если все проверки пройдены
        return response
    }
}