package com.example.dormproject.adapters.repair

import com.example.dormproject.retrofit.req.repair.data.ReqRepairGetAllRepairsListItem

// Дата-класс для хранения данных адаптера ремонтов
data class ItemRepairAdapterDataClass(
    // Список элементов ремонтов
    val items: List<ReqRepairGetAllRepairsListItem>,
    // Лямбда-функция для обработки нажатия на кнопку удаления
    val onClickDelete: (Int) -> Unit,
    // Лямбда-функция для обработки нажатия на кнопку редактирования
    val onClickEdit: (ReqRepairGetAllRepairsListItem) -> Unit
)