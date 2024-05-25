package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormproject.retrofit.req.repair.RepairApi
import com.example.dormproject.retrofit.req.repair.ReqRepairGetAllRepairsListItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeneralRepairsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_general_repairs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navBar = findViewById<BottomNavigationView>(R.id.nav_bar)

        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_guests -> {
                    val newIntent = Intent(this@GeneralRepairsActivity, GeneralGuestsActivity::class.java)
                    startActivity(newIntent)
                    true
                }

                R.id.menu_repairs -> {
                    val newIntent = Intent(this@GeneralRepairsActivity, GeneralRepairsActivity::class.java)
                    startActivity(newIntent)
                    true
                }

                R.id.menu_logout -> {
                    Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@GeneralRepairsActivity, MainActivity::class.java))
                    true
                }

                else -> false
            }
        }

        val itemList: RecyclerView = findViewById(R.id.repairList)

        setNewList(getRepairApi(), itemList)

        val addRepairButton = findViewById<Button>(R.id.general_repair_add)
        addRepairButton.setOnClickListener {
            startActivity(Intent(this@GeneralRepairsActivity, AddRepairActivity::class.java))
        }
    }

    fun getRepairApi(): RepairApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor()).addInterceptor(CookieInterceptor())
            .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val result = retrofit.create(RepairApi::class.java)

        return result
    }

    fun deleteItem(id: Int) {

        val itemList: RecyclerView = findViewById(R.id.repairList)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                getRepairApi()
                    .reqGuestDeleteRepairByIdRequest(id)
                runOnUiThread {
                    Toast.makeText(
                        this@GeneralRepairsActivity, "Заявка удалена", Toast.LENGTH_SHORT
                    ).show()
                    setNewList(getRepairApi(), itemList)
                }
            } catch (e: Exception) {
                val newE = e.toString().replace("java.io.IOException: ", "")
                Toast.makeText(
                    this@GeneralRepairsActivity,
                    "Проблема с удалением, Код ошибки: $newE",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setNewList(
        repairApi: RepairApi, itemList: RecyclerView
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repairApi.reqRepairGetAllRepairs()
                runOnUiThread {
                    val data: ItemRepairAdapterDataClass =
                        ItemRepairAdapterDataClass(response.guestRequestList as ArrayList<ReqRepairGetAllRepairsListItem>,
                            onClickDelete = { id ->
                                deleteItem(id)
                                setNewList(repairApi, itemList)
                            }
                        ) { item ->
                            val newIntent =
                                Intent(this@GeneralRepairsActivity, EditRepairActivity::class.java)
                            newIntent.putExtra("itemID", item.reqId)
                            startActivity(newIntent)
                        }
                    itemList.layoutManager = LinearLayoutManager(applicationContext)
                    itemList.adapter = ItemRepairAdapter(data)
                    if (response.guestRequestList.isEmpty()) {
                        Toast.makeText(
                            this@GeneralRepairsActivity, "Заявок нет", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    val newE = e.toString().replace("java.io.IOException: ", "")
                    Toast.makeText(
                        this@GeneralRepairsActivity,
                        "Проблема с подключением, Код ошибки: $newE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


