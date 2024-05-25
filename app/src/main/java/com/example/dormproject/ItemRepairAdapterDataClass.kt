package com.example.dormproject

import com.example.dormproject.retrofit.req.repair.ReqRepairGetAllRepairsListItem

data class ItemRepairAdapterDataClass(
    var items: ArrayList<ReqRepairGetAllRepairsListItem>,
    var onClickDelete: (Int) -> Unit,
    var onClickEdit: (ReqRepairGetAllRepairsListItem) -> Unit
)