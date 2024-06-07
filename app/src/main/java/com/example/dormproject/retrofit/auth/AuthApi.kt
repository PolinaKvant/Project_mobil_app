package com.example.dormproject.retrofit.auth

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Интерфейс для API-запросов, связанных с аутентификацией и регистрацией пользователей.
 */
interface AuthApi {

    /**
     * POST-запрос для авторизации пользователя.
     * @param body Тело запроса, содержащее данные для авторизации.
     * @return Ответ с данными авторизации.
     */
    @POST("auth/login")
    suspend fun authLogin(@Body body: AuthLoginRequest): AuthLoginResponse

    /**
     * POST-запрос для регистрации нового пользователя.
     * @param body Тело запроса, содержащее данные для регистрации.
     * @return Ответ с данными регистрации.
     */
    @POST("auth/reg")
    suspend fun authReg(@Body body: AuthRegRequest): AuthRegResponse
}