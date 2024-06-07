package com.example.dormproject.retrofit.req.repair

import com.example.dormproject.retrofit.req.repair.data.*
import retrofit2.http.*

/**
 * Интерфейс для API-запросов, связанных с ремонтными заявками.
 */
interface RepairApi {

    /**
     * GET-запрос для получения всех ремонтных заявок.
     * @return Ответ с данными всех ремонтных заявок.
     */
    @GET("req/repair")
    suspend fun reqRepairGetAllRepairs(): ReqRepairGetAllRepairsResponse

    /**
     * GET-запрос для получения ремонтной заявки по ID.
     * @param id Идентификатор ремонтной заявки.
     * @return Ответ с данными ремонтной заявки.
     */
    @GET("req/repair/{id}")
    suspend fun reqRepairGetRepairById(@Path("id") id: Int): ReqRepairGetRepairByIdResponse

    /**
     * POST-запрос для создания новой ремонтной заявки.
     * @param body Тело запроса с данными для создания заявки.
     * @return Ответ с данными созданной заявки.
     */
    @POST("req/repair")
    suspend fun reqRepairCreateRepairRequest(@Body body: ReqRepairCreateRepairRequest): ReqRepairCreateRepairResponse

    /**
     * DELETE-запрос для удаления ремонтной заявки по ID.
     * @param id Идентификатор ремонтной заявки.
     */
    @DELETE("req/repair/{id}")
    suspend fun reqRepairDeleteRepairByIdRequest(@Path("id") id: Int)

    /**
     * PATCH-запрос для редактирования ремонтной заявки по ID.
     * @param id Идентификатор ремонтной заявки.
     * @param body Тело запроса с данными для редактирования заявки.
     * @return Ответ с данными отредактированной заявки.
     */
    @PATCH("req/repair/{id}")
    suspend fun reqRepairEditGuestById(@Path("id") id: Int, @Body body: ReqRepairEditRepairByIdRequest): ReqRepairEditRepairByIdResponse
}