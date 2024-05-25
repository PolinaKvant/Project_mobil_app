package com.example.dormproject.retrofit.auth

class AuthRegRequest(
    val login: String,
    val password: String,
    val dormId: Int,
    val roleId: Int
)