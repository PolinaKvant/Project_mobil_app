package com.example.dormproject.adapters.repair

import com.example.dormproject.retrofit.req.repair.data.ReqRepairGetAllRepairsListItem

data class ItemRepairAdapterDataClass(
    val items: List<ReqRepairGetAllRepairsListItem>,
    val onClickDelete: (Int) -> Unit,
    val onClickEdit: (ReqRepairGetAllRepairsListItem) -> Unit
)