package com.example.dormproject

import com.example.dormproject.retrofit.req.guest.ReqGuestGetAllGuestsListItem

data class ItemGuestAdapterDataClass(
    var items: List<ReqGuestGetAllGuestsListItem>,
    var onClickDelete: (Int) -> Unit,
    var onClickEdit: (ReqGuestGetAllGuestsListItem) -> Unit
)
