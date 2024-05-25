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
import com.example.dormproject.retrofit.req.guest.GuestApi
import com.example.dormproject.retrofit.req.guest.ReqGuestGetAllGuestsListItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeneralGuestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_general_guests)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val navBar = findViewById<BottomNavigationView>(R.id.nav_bar_guest)

        navBar.selectedItemId = R.id.menu_guests

        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_guests -> {
                    val newIntent = Intent(this@GeneralGuestsActivity, GeneralGuestsActivity::class.java)
                    startActivity(newIntent)
                    true
                }

                R.id.menu_repairs -> {
                    val newIntent = Intent(this@GeneralGuestsActivity, GeneralRepairsActivity::class.java)
                    startActivity(newIntent)
                    true
                }

                R.id.menu_logout -> {
                    Toast.makeText(this@GeneralGuestsActivity, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                else -> false
            }
        }

        val itemList: RecyclerView = findViewById(R.id.guestList)

        setNewList(getGuestApi(), itemList)

        val addGuestButton = findViewById<Button>(R.id.general_guest_add)
        addGuestButton.setOnClickListener {
            startActivity(Intent(this@GeneralGuestsActivity, AddGuestActivity::class.java))
        }
    }

    fun getGuestApi(): GuestApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor()).addInterceptor(CookieInterceptor())
            .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val result = retrofit.create(GuestApi::class.java)

        return result
    }

    fun deleteItem(id: Int) {

        val itemList: RecyclerView = findViewById(R.id.guestList)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                getGuestApi()
                    .reqGuestDeleteGuestByIdRequest(id)
                runOnUiThread {
                    Toast.makeText(
                        this@GeneralGuestsActivity, "Заявка удалена", Toast.LENGTH_SHORT
                    ).show()
                    setNewList(getGuestApi(), itemList)
                }
            } catch (e: Exception) {
                val newE = e.toString().replace("java.io.IOException: ", "")
                Toast.makeText(
                    this@GeneralGuestsActivity,
                    "Проблема с удалением, Код ошибки: $newE",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setNewList(
        GuestApi: GuestApi, itemList: RecyclerView
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = GuestApi.reqGuestGetAllGuests()
                runOnUiThread {
                    val data: ItemGuestAdapterDataClass =
                        ItemGuestAdapterDataClass(response.guestRequestList as ArrayList<ReqGuestGetAllGuestsListItem>,
                            onClickDelete = { id ->
                                deleteItem(id)
                                setNewList(GuestApi, itemList)
                            }
                        ) { item ->
                            val newIntent =
                                Intent(this@GeneralGuestsActivity, EditGuestActivity::class.java)
                            newIntent.putExtra("itemID", item.reqId)
                            startActivity(newIntent)
                        }
                    itemList.layoutManager = LinearLayoutManager(applicationContext)
                    itemList.adapter = ItemGuestAdapter(data)
                    if (response.guestRequestList.isEmpty()) {
                        Toast.makeText(
                            this@GeneralGuestsActivity, "Заявок нет", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    val newE = e.toString().replace("java.io.IOException: ", "")
                    Toast.makeText(
                        this@GeneralGuestsActivity,
                        "Проблема с подключением, Код ошибки: $newE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}