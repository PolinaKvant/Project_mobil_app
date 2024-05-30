package com.example.dormproject.retrofit.req.repair.data

//{
//  "reqId": 5432,
//  "title": "Повесить полку",
//  "responsible": 0,
//  "typeId": 2,
//  "description": "4 этаж 426 комната",
//  "statusId": 0
//}

data class ReqRepairCreateRepairResponse(
    val reqId: Int,
    val title: String,
    val responsible: Int,
    val typeId: Int,
    val description: String,
    val statusId: Int
)
