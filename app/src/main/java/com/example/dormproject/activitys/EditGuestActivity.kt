package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.ApiService
import com.example.dormproject.R
import com.example.dormproject.retrofit.req.guest.GuestApi
import com.example.dormproject.retrofit.req.guest.data.ReqGuestEditGuestByIdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditGuestActivity : AppCompatActivity() {

    private val guestApi: GuestApi by lazy { ApiService.createService(GuestApi::class.java) }
    private val fullNameInput by lazy { findViewById<android.widget.EditText>(R.id.edit_guest_fullName) }
    private val dateInput by lazy { findViewById<android.widget.EditText>(R.id.edit_guest_date) }
    private val timeFromInput by lazy { findViewById<android.widget.EditText>(R.id.edit_guest_timeFrom) }
    private val timeToInput by lazy { findViewById<android.widget.EditText>(R.id.edit_guest_timeTo) }
    private val statusInput by lazy { findViewById<android.widget.EditText>(R.id.edit_guest_statusId) }
    private val cancelButton by lazy { findViewById<android.widget.Button>(R.id.edit_guest_cancel) }
    private val submitButton by lazy { findViewById<android.widget.Button>(R.id.edit_guest_submit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_guest)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId = intent.getSerializableExtra("itemID") as? Int

        itemId?.let {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = guestApi.reqGuestGetGuestById(it)
                    withContext(Dispatchers.Main) {
                        response.guestRequestList[0].apply {
                            fullNameInput.setText(fullName)
                            dateInput.setText(date)
                            timeFromInput.setText(timeFrom)
                            timeToInput.setText(timeTo)
                            statusInput.setText(statusId.toString())
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showToast("Проблема с удалением, Код ошибки: ${e.message}")
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralGuestsActivity::class.java))
        }

        submitButton.setOnClickListener {
            if (validateInputs()) {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        guestApi.reqGuestEditGuestById(
                            itemId!!, ReqGuestEditGuestByIdRequest(
                                fullNameInput.text.toString(),
                                dateInput.text.toString(),
                                timeFromInput.text.toString(),
                                timeToInput.text.toString(),
                                statusInput.text.toString().toInt(),
                            )
                        )
                        showToast("Запись обновлена")
                        startActivity(Intent(this@EditGuestActivity, GeneralGuestsActivity::class.java))
                    } catch (e: Exception) {
                        showToast("Произошла ошибка: ${e.message}")
                        startActivity(Intent(this@EditGuestActivity, GeneralRepairsActivity::class.java))
                    }
                }
            }
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
        if (statusInput.text.isEmpty()) {
            statusInput.error = "Заполните поле"
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
