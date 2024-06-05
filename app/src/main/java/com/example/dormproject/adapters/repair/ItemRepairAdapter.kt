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

class ItemRepairAdapter(var data: ItemRepairAdapterDataClass) : RecyclerView.Adapter<ItemRepairAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_repair_title)
        val description: TextView = view.findViewById(R.id.item_repair_description)
        val status: TextView = view.findViewById(R.id.item_repair_status)
        val delete: Button = view.findViewById(R.id.item_repair_delete)
        val edit: Button = view.findViewById(R.id.item_repair_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repair, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = data.items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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
        val item = data.items[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.status.text = when (item.statusId) {
            0 -> "Создана"
            3 -> "Архивирована"
            4 -> "Выполнена"
            5 -> "В работе"
            else -> item.statusId.toString()
        }

        holder.delete.setOnClickListener { data.onClickDelete(item.reqId) }
        holder.edit.setOnClickListener { data.onClickEdit(item) }
    }
}
