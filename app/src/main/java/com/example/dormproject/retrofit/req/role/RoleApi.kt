package com.example.dormproject.retrofit.req.role

import com.example.dormproject.retrofit.req.role.data.ReqRoleGetMyRoleResponse
import retrofit2.http.GET

// Интерфейс для API-запросов, связанных с ролями пользователя
interface RoleApi {
    // Определение GET-запроса к endpoint "req/role/getMyRole"
    @GET("req/role/getMyRole")
    suspend fun getMyRole(): ReqRoleGetMyRoleResponse
}