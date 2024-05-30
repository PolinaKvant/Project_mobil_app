package com.example.dormproject.adapters.repair

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormproject.R

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
        val item = data.items[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.status.text = when (item.statusId) {
            0 -> "Создана"
            1 -> "В работе"
            2 -> "Готово"
            3 -> "Отклонено"
            4 -> "Отменена"
            else -> item.statusId.toString()
        }

        holder.delete.setOnClickListener { data.onClickDelete(item.reqId) }
        holder.edit.setOnClickListener { data.onClickEdit(item) }
    }
}
