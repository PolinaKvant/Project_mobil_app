package com.example.dormproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemGuestAdapter(var data: ItemGuestAdapterDataClass) : RecyclerView.Adapter<ItemGuestAdapter.MyViewHolder>()  {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fullName: TextView = view.findViewById(R.id.item_guest_fullName)
        val date: TextView = view.findViewById(R.id.item_guest_date)
        val dateTime: TextView = view.findViewById(R.id.item_guest_time)
        val status: TextView = view.findViewById(R.id.item_guest_status)

        val delete: Button = view.findViewById(R.id.item_guest_delete)
        val edit: Button = view.findViewById(R.id.item_guest_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guest, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data.items[position]
        holder.fullName.text = item.fullName
        holder.date.text = item.date
        holder.dateTime.text = "${item.timeFrom} ${item.timeTo}"
        holder.status.text = item.statusId.toString()

        holder.delete.setOnClickListener {
            data.onClickDelete(item.reqId)
        }
        holder.edit.setOnClickListener {
            data.onClickEdit(item)
        }
    }

}