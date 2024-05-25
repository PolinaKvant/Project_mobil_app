package com.example.dormproject.retrofit.req.guest

//{
//  "requests": [
//    {
//      "reqId": 5432,
//      "host": 0,
//      "fullName": "Иванов Иван Иванович",
//      "date": "2025-01-01",
//      "timeFrom": "10:00:00",
//      "timeTo": "18:00:00",
//      "statusId": 0
//    }
//  ]
//}

data class ReqGuestGetAllGuestsResponse (
    val guestRequestList: List<ReqGuestGetAllGuestsListItem>
)