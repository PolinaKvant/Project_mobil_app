package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.retrofit.req.guest.GuestApi
import com.example.dormproject.retrofit.req.guest.ReqGuestEditGuestByIdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditGuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_guest)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId = intent.getSerializableExtra("itemID")

        if(itemId != null) {
            CoroutineScope(Dispatchers.IO).launch {
               try {
                   val response = getGuestApi().reqGuestGetGuestById(itemId.toString().toInt())

                   runOnUiThread {
                       findViewById<android.widget.EditText>(R.id.edit_guest_fullName).setText(response.guestRequestList[0].fullName)
                       findViewById<android.widget.EditText>(R.id.edit_guest_date).setText(response.guestRequestList[0].date)
                       findViewById<android.widget.EditText>(R.id.edit_guest_timeFrom).setText(response.guestRequestList[0].timeFrom)
                       findViewById<android.widget.EditText>(R.id.edit_guest_timeTo).setText(response.guestRequestList[0].timeTo)
                       findViewById<android.widget.EditText>(R.id.edit_guest_statusId).setText(response.guestRequestList[0].statusId.toString())
                   }
               } catch (e: Exception) {
                   val newE = e.toString().replace("java.io.IOException: ", "")
                   Toast.makeText(
                       this@EditGuestActivity,
                       "Проблема с удалением, Код ошибки: $newE",
                       Toast.LENGTH_SHORT
                   ).show()
               }

            }
        }

        val fullNameInput = findViewById<android.widget.EditText>(R.id.edit_guest_fullName)
        val dateInput = findViewById<android.widget.EditText>(R.id.edit_guest_date)
        val timeFromInput = findViewById<android.widget.EditText>(R.id.edit_guest_timeFrom)
        val timeToInput = findViewById<android.widget.EditText>(R.id.edit_guest_timeTo)
        val statusInput = findViewById<android.widget.EditText>(R.id.edit_guest_statusId)


        val cancelButton = findViewById<android.widget.Button>(R.id.edit_guest_cancel)
        val submitButton = findViewById<android.widget.Button>(R.id.edit_guest_submit)

        cancelButton.setOnClickListener {
            startActivity(Intent(this@EditGuestActivity, GeneralGuestsActivity::class.java))
        }


        submitButton.setOnClickListener {
            if (fullNameInput.text.isEmpty() || dateInput.text.isEmpty() || timeFromInput.text.isEmpty() || timeToInput.text.isEmpty() || statusInput.text.isEmpty()) {
                Toast.makeText(this@EditGuestActivity, "Заполните все поля", Toast.LENGTH_SHORT)
                    .show()

                if (fullNameInput.text.isEmpty()) {
                    fullNameInput.error = "Заполните поле"
                }
                if (dateInput.text.isEmpty()) {
                    dateInput.error = "Заполните поле"
                }
                if (timeFromInput.text.isEmpty()) {
                    timeFromInput.error = "Заполните поле"
                }
                if (timeToInput.text.isEmpty()) {
                    timeToInput.error = "Заполните поле"
                }
                if (statusInput.text.isEmpty()) {
                    statusInput.error = "Заполните поле"
                }

                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    getGuestApi().reqGuestEditGuestById(
                        itemId.toString().toInt(), ReqGuestEditGuestByIdRequest(
                            fullNameInput.text.toString(),
                            dateInput.text.toString(),
                            timeFromInput.text.toString(),
                            timeToInput.text.toString(),
                            statusInput.text.toString().toInt(),
                        )
                    )


                    runOnUiThread {
                        Toast.makeText(
                            this@EditGuestActivity,
                            "Запись обновлена",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@EditGuestActivity, GeneralGuestsActivity::class.java
                            )
                        )
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@EditGuestActivity,
                        "Произошла ошибка $e",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@EditGuestActivity, GeneralRepairsActivity::class.java
                        )
                    )
                }
            }
            startActivity(Intent(this@EditGuestActivity, GeneralGuestsActivity::class.java))
        }
    }
}

fun getGuestApi(): GuestApi {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor())
        .addInterceptor(CookieInterceptor()).addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val result = retrofit.create(GuestApi::class.java)

    return result
}