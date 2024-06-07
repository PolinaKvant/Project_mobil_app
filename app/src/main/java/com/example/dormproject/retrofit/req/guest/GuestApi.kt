package com.example.dormproject.retrofit.req.guest

import com.example.dormproject.retrofit.req.guest.data.*
import retrofit2.http.*

/**
 * Интерфейс для API-запросов, связанных с гостевыми заявками.
 */
interface GuestApi {

    /**
     * GET-запрос для получения гостевой заявки по ID.
     * @param id Идентификатор гостевой заявки.
     * @return Ответ с данными гостевой заявки.
     */
    @GET("req/guest/{id}")
    suspend fun reqGuestGetGuestById(@Path("id") id: Int): ReqGuestGetGuestByIdResponse

    /**
     * GET-запрос для получения всех гостевых заявок.
     * @return Ответ с данными всех гостевых заявок.
     */
    @GET("req/guest")
    suspend fun reqGuestGetAllGuests(): ReqGuestGetAllGuestsResponse

    /**
     * PATCH-запрос для редактирования гостевой заявки по ID.
     * @param id Идентификатор гостевой заявки.
     * @param body Тело запроса с данными для редактирования заявки.
     * @return Ответ с данными отредактированной заявки.
     */
    @PATCH("req/guest/{id}")
    suspend fun reqGuestEditGuestById(@Path("id") id: Int, @Body body: ReqGuestEditGuestByIdRequest): ReqGuestEditGuestByIdResponse

    /**
     * DELETE-запрос для удаления гостевой заявки по ID.
     * @param id Идентификатор гостевой заявки.
     */
    @DELETE("req/guest/{id}")
    suspend fun reqGuestDeleteGuestByIdRequest(@Path("id") id: Int)

    /**
     * POST-запрос для создания новой гостевой заявки.
     * @param body Тело запроса с данными для создания заявки.
     * @return Ответ с данными созданной заявки.
     */
    @POST("req/guest")
    suspend fun reqGuestCreateGuest(@Body body: ReqGuestCreateGuestRequest): ReqGuestCreateGuestResponse
}