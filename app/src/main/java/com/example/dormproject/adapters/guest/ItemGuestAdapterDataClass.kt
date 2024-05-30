package com.example.dormproject.adapters.guest

import com.example.dormproject.retrofit.req.guest.data.ReqGuestGetAllGuestsListItem

data class ItemGuestAdapterDataClass(
    val items: List<ReqGuestGetAllGuestsListItem>,
    val onClickDelete: (Int) -> Unit,
    val onClickEdit: (ReqGuestGetAllGuestsListItem) -> Unit
)
