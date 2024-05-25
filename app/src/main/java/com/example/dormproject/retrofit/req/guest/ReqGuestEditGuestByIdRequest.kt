package com.example.dormproject.retrofit.req.guest

//{
//  "fullName": "Иванов Иван Иванович",
//  "date": "2025-01-01",
//  "timeFrom": "10:00:00",
//  "timeTo": "18:00:00",
//  "statusId": 0
//}

data class ReqGuestEditGuestByIdRequest (
    val fullName: String,
    val date: String,
    val timeFrom: String,
    val timeTo: String,
    val statusId: Int
)