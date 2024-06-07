package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.ApiService
import com.example.dormproject.R
import com.example.dormproject.retrofit.req.repair.RepairApi
import com.example.dormproject.retrofit.req.repair.data.ReqRepairEditRepairByIdRequest
import com.example.dormproject.retrofit.req.repair.data.ReqRepairGetRepairByIdResponse
import com.example.dormproject.retrofit.req.role.RoleApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRepairActivity : AppCompatActivity() {

    // Ленивая инициализация API для ролей и ремонтов
    private val roleApi: RoleApi by lazy { ApiService.createService(RoleApi::class.java) }
    private val repairApi: RepairApi by lazy { ApiService.createService(RepairApi::class.java) }

    // Ленивая инициализация полей ввода и кнопок
    private val titleInput: EditText by lazy { findViewById(R.id.edit_repair_title) }
    private val descriptionInput: EditText by lazy { findViewById(R.id.edit_repair_description) }
    private val statusSpinner: Spinner by lazy { findViewById(R.id.edit_repair_status) }
    private val titleInputText: View? by lazy { findViewById(R.id.edit_repair_title_text) }
    private val descriptionInputText: View? by lazy { findViewById(R.id.edit_repair_description_text) }
    private val statusInputText: View? by lazy { findViewById(R.id.edit_repair_status_text) }
    private val cancelButton: Button by lazy { findViewById(R.id.edit_repair_cancel) }
    private val submitButton: Button by lazy { findViewById(R.id.edit_repair_submit) }

    // Метод onCreate, вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Включение поддержки отступов для системных окон
        setContentView(R.layout.activity_edit_repair)
        setupSpinner() // Настройка спиннера для выбора статуса
        setupWindowInsets() // Настройка отступов для системных окон

        // Получение ID элемента из интента
        val itemId = intent.getSerializableExtra("itemID") as? Int
        itemId?.let {
            loadData(it) // Загрузка данных о ремонте по ID
        }

        // Обработка нажатия кнопки отмены
        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
            finish()
        }

        // Обработка нажатия кнопки отправки
        submitButton.setOnClickListener {
            itemId?.let {
                submitData(it) // Отправка данных для обновления информации о ремонте
            }
        }
    }

    // Настройка отступов для системных окон
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Загрузка данных о ремонте по ID
    private fun loadData(itemId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repair = repairApi.reqRepairGetRepairById(itemId)
                val response = roleApi.getMyRole()

                withContext(Dispatchers.Main) {
                    populateFields(repair, response.roleId) // Заполнение полей ввода данными о ремонте
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Ошибка загрузки данных: ${e.message}")
                }
            }
        }
    }

    // Настройка спиннера для выбора статуса
    private fun setupSpinner() {
        val listGuestStatuses = listOf("Создана", "Архивирована", "Выполнена", "В работе")
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listGuestStatuses)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = arrayAdapter
    }

    // Заполнение полей ввода данными о ремонте
    private fun populateFields(repair: ReqRepairGetRepairByIdResponse, roleId: Int) {
        if (repair.guestRequestList.isEmpty()) {
            showToast("Невозможно отредактировать запись")
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
            finish()
        } else {
            repair.guestRequestList[0].apply {
                titleInput.setText(title)
                descriptionInput.setText(description)
                statusSpinner.setSelection(getStatusSelection(statusId))
            }

            setVisibility(
                roleId != 1,
                statusSpinner,
                statusInputText,
            )
            setVisibility(
                roleId != 3,
                titleInput,
                titleInputText,
                descriptionInput,
                descriptionInputText,
            )

            if (roleId == 1 && repair.guestRequestList[0].statusId != 0) {
                showToast("Вы не можете редактировать запись")
                startActivity(Intent(this, GeneralRepairsActivity::class.java))
            }
        }
    }

    // Получение позиции для спиннера на основе ID статуса
    private fun getStatusSelection(statusId: Int): Int {
        return when (statusId) {
            0 -> 0
            3 -> 1
            4 -> 2
            else -> 3
        }
    }

    // Установка видимости для переданных представлений
    private fun setVisibility(isVisible: Boolean, vararg views: View?) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        views.forEach { it?.visibility = visibility }
    }

    // Отправка данных для обновления информации о ремонте
    private fun submitData(itemId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val statusId = getStatusIdFromSpinner(statusSpinner.selectedItem.toString())
                val response = repairApi.reqRepairGetRepairById(itemId)

                repairApi.reqRepairEditGuestById(
                    itemId, ReqRepairEditRepairByIdRequest(
                        titleInput.text.toString(),
                        response.guestRequestList[0].responsible,
                        descriptionInput.text.toString(),
                        statusId
                    )
                )
                withContext(Dispatchers.Main) {
                    showToast("Запись обновлена")
                    startActivity(
                        Intent(
                            this@EditRepairActivity,
                            GeneralRepairsActivity::class.java
                        )
                    )
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Ошибка обновления записи: ${e.message}")
                }
            }
        }
    }

    // Получение ID статуса из выбранного элемента спиннера
    private fun getStatusIdFromSpinner(status: String): Int {
        return when (status) {
            "Создана" -> 0
            "Архивирована" -> 3
            "Выполнена" -> 4
            else -> 5
        }
    }

    // Показ сообщения Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
