package com.example.dormproject.retrofit.req.repair.data

data class ReqRepairEditRepairByIdRequest(
    val title: String,
    val responsible: Int,
    val description: String,
    val statusId: Int
)
