package com.example.dormproject.retrofit.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun authLogin(@Body body: AuthLoginRequest): AuthLoginResponse

    @POST("auth/reg")
    suspend fun authReg(@Body body: AuthRegRequest): AuthRegResponse
}