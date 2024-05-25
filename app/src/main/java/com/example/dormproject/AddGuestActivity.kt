package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.retrofit.req.guest.GuestApi
import com.example.dormproject.retrofit.req.guest.ReqGuestCreateGuestRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddGuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guest)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cancelButton = findViewById<Button>(R.id.add_guest_cancel)

        cancelButton.setOnClickListener {
            startActivity(
                Intent(
                    this@AddGuestActivity, GeneralGuestsActivity::class.java
                )
            )
        }

        val fullNameInput = findViewById<EditText>(R.id.add_guest_fullName)
        val dateInput = findViewById<EditText>(R.id.add_guest_date)
        val timeFromInput = findViewById<EditText>(R.id.add_guest_timeFrom)
        val timeToInput = findViewById<EditText>(R.id.add_guest_timeTo)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor())
            .addInterceptor(CookieInterceptor()).addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val guestApi = retrofit.create(GuestApi::class.java)

        val submitButton = findViewById<Button>(R.id.add_guest_submit)

        submitButton.setOnClickListener {
            val fullName = fullNameInput.text.toString()
            val date = dateInput.text.toString()
            val timeFrom = timeFromInput.text.toString()
            val timeTo = timeToInput.text.toString()

            if (fullName.isEmpty() || date.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()

                if (fullName.isEmpty()) {
                    fullNameInput.error = "Заполните поле"
                }
                if (date.isEmpty()) {
                    dateInput.error = "Заполните поле"
                }
                if (timeFrom.isEmpty()) {
                    timeFromInput.error = "Заполните поле"
                }
                if (timeTo.isEmpty()) {
                    timeToInput.error = "Заполните поле"
                }

                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = guestApi.reqGuestCreateGuest(
                        ReqGuestCreateGuestRequest(
                            fullName, date, timeFrom, timeTo
                        )
                    )

                    if (response.fullName.isNotEmpty()) {
                        runOnUiThread {
                            Toast.makeText(
                                this@AddGuestActivity, "Заявка создана", Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@AddGuestActivity, GeneralGuestsActivity::class.java
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    val newE = e.toString().replace("java.io.IOException: ", "")
                    Toast.makeText(
                        this@AddGuestActivity,
                        "Проблема с подключением, Код ошибки: $newE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}