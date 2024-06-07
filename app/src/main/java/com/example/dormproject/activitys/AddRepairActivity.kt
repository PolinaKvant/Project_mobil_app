package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.R
import com.example.dormproject.ApiService
import com.example.dormproject.retrofit.req.repair.RepairApi
import com.example.dormproject.retrofit.req.repair.data.ReqRepairCreateRepairRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRepairActivity : AppCompatActivity() {

    // Ленивая инициализация API для работы с заявками на ремонт
    private val repairApi: RepairApi by lazy { ApiService.createService(RepairApi::class.java) }

    // Ленивая инициализация полей ввода и кнопок
    private val titleInput by lazy { findViewById<EditText>(R.id.add_repair_title) }
    private val descriptionInput by lazy { findViewById<EditText>(R.id.add_repair_description) }
    private val cancelButton by lazy { findViewById<Button>(R.id.add_repair_cancel) }
    private val submitButton by lazy { findViewById<Button>(R.id.add_repair_submit) }

    // Метод onCreate, вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Включение поддержки отступов для системных окон
        setContentView(R.layout.activity_add_repair)

        // Установка отступов для системных окон
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Обработка нажатия кнопки отмены
        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
        }

        // Обработка нажатия кнопки отправки
        submitButton.setOnClickListener {
            // Проверка валидности введенных данных
            if (validateInputs()) {
                // Запуск корутины для выполнения сетевого запроса
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Отправка запроса на создание нового ремонта
                        val response = repairApi.reqRepairCreateRepairRequest(
                            ReqRepairCreateRepairRequest(
                                titleInput.text.toString(),
                                descriptionInput.text.toString(),
                                2
                            )
                        )

                        // Обработка ответа и отображение результата в UI потоке
                        withContext(Dispatchers.Main) {
                            if (response.title.isNotEmpty()) {
                                Toast.makeText(this@AddRepairActivity, "Заявка создана", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AddRepairActivity, GeneralRepairsActivity::class.java))
                            }
                        }
                    } catch (e: Exception) {
                        // Обработка ошибки и отображение сообщения
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddRepairActivity, "Проблема с подключением, Код ошибки: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    // Метод для проверки валидности введенных данных
    private fun validateInputs(): Boolean {
        var isValid = true

        // Проверка поля ввода названия
        if (titleInput.text.isEmpty()) {
            titleInput.error = "Заполните это поле"
            isValid = false
        }

        // Проверка поля ввода описания
        if (descriptionInput.text.isEmpty()) {
            descriptionInput.error = "Заполните это поле"
            isValid = false
        }

        // Если какое-то поле не заполнено, отображение сообщения Toast
        if (!isValid) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }
}
