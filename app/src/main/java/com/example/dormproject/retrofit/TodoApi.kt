package com.example.dormproject.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface TodoApi {
    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): Todo
}