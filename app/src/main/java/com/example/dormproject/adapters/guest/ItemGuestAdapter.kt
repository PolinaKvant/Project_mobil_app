package com.example.dormproject.adapters.guest

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

class ItemGuestAdapter(var data: ItemGuestAdapterDataClass) :
    RecyclerView.Adapter<ItemGuestAdapter.MyViewHolder>() {

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
        holder.fullName.text = item.fullName
        holder.date.text = item.date
        holder.dateTime.text = "${item.timeFrom} ${item.timeTo}"
        holder.status.text = when (item.statusId) {
            0 -> "Создана"
            1 -> "Принята"
            2 -> "Отклонена"
            3 -> "Архивирована"
            else -> item.statusId.toString()
        }

        holder.delete.setOnClickListener { data.onClickDelete(item.reqId) }
        holder.edit.setOnClickListener { data.onClickEdit(item) }
    }
}
