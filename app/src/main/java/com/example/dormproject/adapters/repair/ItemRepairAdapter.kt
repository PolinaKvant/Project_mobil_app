package com.example.dormproject.adapters.repair

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormproject.ApiService
import com.example.dormproject.R
import com.example.dormproject.retrofit.req.role.RoleApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Адаптер для отображения списка ремонтов в RecyclerView
class ItemRepairAdapter(var data: ItemRepairAdapterDataClass) : RecyclerView.Adapter<ItemRepairAdapter.MyViewHolder>() {

    // Вложенный класс ViewHolder для хранения ссылок на виджеты
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_repair_title) // Поле для отображения заголовка ремонта
        val description: TextView = view.findViewById(R.id.item_repair_description) // Поле для отображения описания ремонта
        val status: TextView = view.findViewById(R.id.item_repair_status) // Поле для отображения статуса ремонта
        val delete: Button = view.findViewById(R.id.item_repair_delete) // Кнопка для удаления ремонта
        val edit: Button = view.findViewById(R.id.item_repair_edit) // Кнопка для редактирования ремонта
    }

    // Создание нового ViewHolder при создании элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repair, parent, false)
        return MyViewHolder(view)
    }

    // Возвращает количество элементов в списке
    override fun getItemCount(): Int = data.items.size

    // Привязка данных к виджетам ViewHolder при прокрутке списка
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Запрос к API для получения роли пользователя и скрытия кнопки удаления, если пользователь не имеет прав
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.createService(RoleApi::class.java).getMyRole()
                withContext(Dispatchers.Main) {
                    if (response.roleId == 3) {
                        holder.delete.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val newE = e.toString().replace("java.io.IOException: ", "")
                }
            }
        }

        // Получение текущего элемента списка
        val item = data.items[position]

        // Установка текста заголовка, описания и статуса ремонта
        holder.title.text = item.title
        holder.description.text = item.description
        holder.status.text = when (item.statusId) {
            0 -> "Создана"
            3 -> "Архивирована"
            4 -> "Выполнена"
            5 -> "В работе"
            else -> item.statusId.toString()
        }

        // Установка обработчиков нажатий на кнопки удаления и редактирования
        holder.delete.setOnClickListener { data.onClickDelete(item.reqId) }
        holder.edit.setOnClickListener { data.onClickEdit(item) }
    }
}