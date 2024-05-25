package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.retrofit.req.repair.RepairApi
import com.example.dormproject.retrofit.req.repair.ReqRepairEditRepairByIdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditRepairActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_repair)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titleInput = findViewById<android.widget.EditText>(R.id.edit_repair_title)
        val descriptionInput = findViewById<android.widget.EditText>(R.id.edit_repair_description)
        val status = findViewById<android.widget.EditText>(R.id.edit_repair_status)
        val responsible = findViewById<android.widget.EditText>(R.id.edit_repair_responsible)

        val cancelButton = findViewById<android.widget.Button>(R.id.edit_repair_cancel)
        val submitButton = findViewById<android.widget.Button>(R.id.edit_repair_submit)

        val itemId = intent.getSerializableExtra("itemID")

        if (itemId != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val repair = getRepairApi().reqRepairGetRepairById(itemId.toString().toInt())

                runOnUiThread {
                    titleInput.setText(repair.guestRequestList[0].title)
                    descriptionInput.setText(repair.guestRequestList[0].description)
                    status.setText(repair.guestRequestList[0].statusId.toString())
                    responsible.setText(repair.guestRequestList[0].responsible.toString())
                }
            }
        }


        cancelButton.setOnClickListener {
            startActivity(Intent(this@EditRepairActivity, GeneralRepairsActivity::class.java))
        }


        submitButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                getRepairApi().reqGuestEditGuestById(
                    itemId.toString().toInt(), ReqRepairEditRepairByIdRequest(
                        titleInput.text.toString(),
                        responsible.text.toString().toInt(),
                        descriptionInput.text.toString(),
                        status.text.toString().toInt()
                    )
                )

                runOnUiThread {
                    Toast.makeText(this@EditRepairActivity, "Запись обновлена", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@EditRepairActivity, GeneralRepairsActivity::class.java))
                }
            }
        }

    }
}

fun getRepairApi(): RepairApi {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor())
        .addInterceptor(CookieInterceptor()).addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val result = retrofit.create(RepairApi::class.java)

    return result
}