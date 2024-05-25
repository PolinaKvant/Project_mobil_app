package com.example.dormproject.retrofit.req.repair

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RepairApi {
    @POST("req/repair")
    suspend fun reqRepairCreateRepairRequest(@Body body: ReqRepairCreateRepairRequest): ReqRepairCreateRepairResponse

    @DELETE("req/repair/{id}")
    suspend fun reqGuestDeleteRepairByIdRequest(@Path("id") id: Int)

    @PATCH("req/repair/{id}")
    suspend fun reqGuestEditGuestById(@Path("id") id: Int, @Body body: ReqRepairEditRepairByIdRequest): ReqRepairEditRepairByIdResponse

    @GET("req/repair")
    suspend fun reqRepairGetAllRepairs(): ReqRepairGetAllRepairsResponse

    @GET("req/repair/{id}")
    suspend fun reqRepairGetRepairById(@Path("id") id: Int): ReqRepairGetRepairByIdResponse
}