package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.retrofit.req.repair.RepairApi
import com.example.dormproject.retrofit.req.repair.ReqRepairCreateRepairRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddRepairActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_repair)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cancelButton = findViewById<Button>(R.id.add_repair_cancel)

        cancelButton.setOnClickListener {
            startActivity(
                Intent(
                    this@AddRepairActivity, GeneralRepairsActivity::class.java
                )
            )
        }

        val titleInput = findViewById<EditText>(R.id.add_repair_title)
        val descriptionInput = findViewById<EditText>(R.id.add_repair_description)

        val submitButton = findViewById<Button>(R.id.add_repair_submit)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor())
            .addInterceptor(CookieInterceptor()).addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val repairApi = retrofit.create(RepairApi::class.java)

        submitButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this@AddRepairActivity, "Заполните все поля", Toast.LENGTH_SHORT)
                    .show()
                if (title.isEmpty()) {
                    titleInput.error = "Заполните это поле"
                }
                if (description.isEmpty()) {
                    descriptionInput.error = "Заполните это поле"
                }
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = repairApi.reqRepairCreateRepairRequest(
                        ReqRepairCreateRepairRequest(
                            title,
                            description,
                            2
                        )
                    )

                    if (response.title.isNotEmpty()) {
                        runOnUiThread {
                            Toast.makeText(
                                this@AddRepairActivity, "Заявка создана", Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@AddRepairActivity, GeneralRepairsActivity::class.java
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    val newE = e.toString().replace("java.io.IOException: ", "")
                    Toast.makeText(
                        this@AddRepairActivity,
                        "Проблема с подключением, Код ошибки: $newE",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }
}