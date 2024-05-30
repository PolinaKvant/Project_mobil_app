package com.example.dormproject.retrofit.req.guest

import com.example.dormproject.retrofit.req.guest.data.ReqGuestCreateGuestRequest
import com.example.dormproject.retrofit.req.guest.data.ReqGuestCreateGuestResponse
import com.example.dormproject.retrofit.req.guest.data.ReqGuestEditGuestByIdRequest
import com.example.dormproject.retrofit.req.guest.data.ReqGuestEditGuestByIdResponse
import com.example.dormproject.retrofit.req.guest.data.ReqGuestGetAllGuestsResponse
import com.example.dormproject.retrofit.req.guest.data.ReqGuestGetGuestByIdResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GuestApi {
    @GET("req/guest/{id}")
    suspend fun reqGuestGetGuestById(@Path("id") id: Int): ReqGuestGetGuestByIdResponse

    @GET("req/guest")
    suspend fun reqGuestGetAllGuests(): ReqGuestGetAllGuestsResponse

    @PATCH("req/guest/{id}")
    suspend fun reqGuestEditGuestById(@Path("id") id: Int, @Body body: ReqGuestEditGuestByIdRequest): ReqGuestEditGuestByIdResponse

    @DELETE("req/guest/{id}")
    suspend fun reqGuestDeleteGuestByIdRequest(@Path("id") id: Int)

    @POST("req/guest")
    suspend fun reqGuestCreateGuest(@Body body: ReqGuestCreateGuestRequest): ReqGuestCreateGuestResponse
}