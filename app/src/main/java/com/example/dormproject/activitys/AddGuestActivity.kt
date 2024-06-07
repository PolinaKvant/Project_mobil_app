package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.R
import com.example.dormproject.ApiService
import com.example.dormproject.retrofit.req.guest.GuestApi
import com.example.dormproject.retrofit.req.guest.data.ReqGuestCreateGuestRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddGuestActivity : AppCompatActivity() {

    // Ленивая инициализация API для работы с гостями
    private val guestApi: GuestApi by lazy { ApiService.createService(GuestApi::class.java) }

    // Ленивая инициализация полей ввода и кнопок
    private val fullNameInput by lazy { findViewById<EditText>(R.id.add_guest_fullName) }
    private val dateInput by lazy { findViewById<EditText>(R.id.add_guest_date) }
    private val timeFromInput by lazy { findViewById<EditText>(R.id.add_guest_timeFrom) }
    private val timeToInput by lazy { findViewById<EditText>(R.id.add_guest_timeTo) }
    private val cancelButton by lazy { findViewById<Button>(R.id.add_guest_cancel) }
    private val submitButton by lazy { findViewById<Button>(R.id.add_guest_submit) }

    // Метод onCreate, вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guest)

        // Установка отступов для системных окон
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Обработка нажатия кнопки отмены
        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralGuestsActivity::class.java))
        }

        // Обработка нажатия кнопки отправки
        submitButton.setOnClickListener {
            // Проверка валидности введенных данных
            if (validateInputs()) {
                // Запуск корутины для выполнения сетевого запроса
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Отправка запроса на создание нового гостя
                        val response = guestApi.reqGuestCreateGuest(
                            ReqGuestCreateGuestRequest(
                                fullNameInput.text.toString(),
                                dateInput.text.toString(),
                                timeFromInput.text.toString(),
                                timeToInput.text.toString()
                            )
                        )

                        // Проверка ответа и отображение результата
                        if (response.fullName.isNotEmpty()) {
                            runOnUiThread {
                                Toast.makeText(this@AddGuestActivity, "Заявка создана", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AddGuestActivity, GeneralGuestsActivity::class.java))
                            }
                        }
                    } catch (e: Exception) {
                        // Обработка ошибки и отображение сообщения
                        runOnUiThread {
                            Toast.makeText(this@AddGuestActivity, "Проблема с подключением, Код ошибки: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    // Метод для проверки валидности введенных данных
    private fun validateInputs(): Boolean {
        var isValid = true

        if (fullNameInput.text.isEmpty()) {
            fullNameInput.error = "Заполните поле"
            isValid = false
        }
        if (dateInput.text.isEmpty()) {
            dateInput.error = "Заполните поле"
            isValid = false
        }
        if (timeFromInput.text.isEmpty()) {
            timeFromInput.error = "Заполните поле"
            isValid = false
        }
        if (timeToInput.text.isEmpty()) {
            timeToInput.error = "Заполните поле"
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }
}
