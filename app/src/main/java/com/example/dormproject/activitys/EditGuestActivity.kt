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
import com.example.dormproject.retrofit.req.guest.GuestApi
import com.example.dormproject.retrofit.req.guest.data.ReqGuestEditGuestByIdRequest
import com.example.dormproject.retrofit.req.guest.data.ReqGuestGetGuestByIdResponse
import com.example.dormproject.retrofit.req.role.RoleApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditGuestActivity : AppCompatActivity() {

    private val roleApi: RoleApi by lazy { ApiService.createService(RoleApi::class.java) }
    private val guestApi: GuestApi by lazy { ApiService.createService(GuestApi::class.java) }
    private val fullNameInput: EditText by lazy { findViewById(R.id.edit_guest_fullName) }
    private val dateInput: EditText by lazy { findViewById(R.id.edit_guest_date) }
    private val timeFromInput: EditText by lazy { findViewById(R.id.edit_guest_timeFrom) }
    private val timeToInput: EditText by lazy { findViewById(R.id.edit_guest_timeTo) }
    private val statusSpinner: Spinner by lazy { findViewById(R.id.edit_guest_statusId) }
    private val fullNameInputText: View? by lazy { findViewById(R.id.edit_guest_fullName_text) }
    private val dateInputText: View? by lazy { findViewById(R.id.edit_guest_date_text) }
    private val timeFromInputText: View? by lazy { findViewById(R.id.edit_guest_timeFrom_text) }
    private val timeToInputText: View? by lazy { findViewById(R.id.edit_guest_timeTo_text) }
    private val statusSpinnerText: View? by lazy { findViewById(R.id.edit_guest_statusId_text) }
    private val cancelButton: Button by lazy { findViewById(R.id.edit_guest_cancel) }
    private val submitButton: Button by lazy { findViewById(R.id.edit_guest_submit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_guest)
        setupSpinner()
        setupWindowInsets()

        val itemId = intent.getSerializableExtra("itemID") as? Int
        itemId?.let {
            loadData(it)
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralGuestsActivity::class.java))
            finish()
        }

        submitButton.setOnClickListener {
            itemId?.let {
                submitData(it)
            }
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadData(itemId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val guest = guestApi.reqGuestGetGuestById(itemId)
                val response = roleApi.getMyRole()

                withContext(Dispatchers.Main) {
                    populateFields(guest, response.roleId)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Ошибка загрузки данных: ${e.message}")
                }
            }
        }
    }

    private fun populateFields(
        guest: ReqGuestGetGuestByIdResponse, roleId: Int
    ) {
        if(guest.guestRequestList.isEmpty()) {
            showToast("Невозможно отредактировать запись")
            startActivity(
                Intent(
                    this@EditGuestActivity,
                    GeneralGuestsActivity::class.java
                )
            )
            finish()
        } else {
            guest.guestRequestList[0].apply {
                fullNameInput.setText(fullName)
                dateInput.setText(date)
                timeFromInput.setText(timeFrom)
                timeToInput.setText(timeTo)
                statusSpinner.setSelection(
                    if (statusId == 0) 0
                    else if (statusId == 1) 1
                    else if (statusId == 2) 2
                    else 3
                )
            }
            setVisibility(roleId != 1, statusSpinner, statusSpinnerText)
            setVisibility(
                roleId != 3,
                fullNameInput,
                fullNameInputText,
                dateInput,
                dateInputText,
                timeFromInput,
                timeFromInputText,
                timeToInput,
                timeToInputText
            )

            if (guest.guestRequestList[0].statusId != 0) {
                showToast("Вы не можете редактировать запись")
                startActivity(Intent(this, GeneralGuestsActivity::class.java))
                finish()
            }
        }
    }

    private fun setVisibility(isVisible: Boolean, vararg views: View?) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        views.forEach { it?.visibility = visibility }
    }

    private fun submitData(itemId: Int) {
        if (validateInputs()) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val statusId = getStatusIdFromSpinner(statusSpinner.selectedItem.toString())
                    guestApi.reqGuestEditGuestById(
                        itemId, ReqGuestEditGuestByIdRequest(
                            fullNameInput.text.toString(),
                            dateInput.text.toString(),
                            timeFromInput.text.toString(),
                            timeToInput.text.toString(),
                            statusId
                        )
                    )
                    showToast("Запись обновлена")
                    startActivity(Intent(this@EditGuestActivity, GeneralGuestsActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    private fun setupSpinner() {
        val listGuestStatuses = listOf("Создана", "Принята", "Отклонена", "Архивирована")
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listGuestStatuses)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = arrayAdapter
    }

    private fun getStatusIdFromSpinner(status: String): Int {
        return when (status) {
            "Создана" -> 0
            "Принята" -> 1
            "Отклонена" -> 2
            else -> 3
        }
    }

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
            showToast("Заполните все поля")
        }

        return isValid
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
