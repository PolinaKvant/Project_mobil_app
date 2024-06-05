package com.example.dormproject.retrofit.req.repair

import com.example.dormproject.retrofit.req.repair.data.ReqRepairCreateRepairRequest
import com.example.dormproject.retrofit.req.repair.data.ReqRepairCreateRepairResponse
import com.example.dormproject.retrofit.req.repair.data.ReqRepairEditRepairByIdRequest
import com.example.dormproject.retrofit.req.repair.data.ReqRepairEditRepairByIdResponse
import com.example.dormproject.retrofit.req.repair.data.ReqRepairGetAllRepairsResponse
import com.example.dormproject.retrofit.req.repair.data.ReqRepairGetRepairByIdResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RepairApi {
    @GET("req/repair")
    suspend fun reqRepairGetAllRepairs(): ReqRepairGetAllRepairsResponse

    @GET("req/repair/{id}")
    suspend fun reqRepairGetRepairById(@Path("id") id: Int): ReqRepairGetRepairByIdResponse

    @POST("req/repair")
    suspend fun reqRepairCreateRepairRequest(@Body body: ReqRepairCreateRepairRequest): ReqRepairCreateRepairResponse

    @DELETE("req/repair/{id}")
    suspend fun reqRepairDeleteRepairByIdRequest(@Path("id") id: Int)

    @PATCH("req/repair/{id}")
    suspend fun reqRepairEditGuestById(@Path("id") id: Int, @Body body: ReqRepairEditRepairByIdRequest): ReqRepairEditRepairByIdResponse
}