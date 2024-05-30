package com.example.dormproject.retrofit.req.guest.data

data class ReqGuestGetAllGuestsListItem (
    val reqId: Int,
    val host: Int,
    val fullName: String,
    val date: String,
    val timeFrom: String,
    val timeTo: String,
    val statusId: Int
)