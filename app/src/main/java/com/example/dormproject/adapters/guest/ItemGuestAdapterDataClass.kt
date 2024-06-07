package com.example.dormproject.adapters.guest

import com.example.dormproject.retrofit.req.guest.data.ReqGuestGetAllGuestsListItem

// Класс данных для хранения списка гостей и обработчиков событий
data class ItemGuestAdapterDataClass(
    // Список элементов типа ReqGuestGetAllGuestsListItem
    val items: List<ReqGuestGetAllGuestsListItem>,
    // Функция-обработчик нажатия кнопки удаления, принимающая ID элемента
    val onClickDelete: (Int) -> Unit,
    // Функция-обработчик нажатия кнопки редактирования, принимающая элемент списка
    val onClickEdit: (ReqGuestGetAllGuestsListItem) -> Unit
)