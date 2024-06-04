package com.example.dormproject.retrofit.req.role

import com.example.dormproject.retrofit.req.role.data.ReqRoleGetMyRoleResponse
import retrofit2.http.GET

interface RoleApi {
    @GET("req/role/getMyRole")
    suspend fun getMyRole(): ReqRoleGetMyRoleResponse
}