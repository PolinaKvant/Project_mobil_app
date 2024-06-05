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

    private val repairApi: RepairApi by lazy { ApiService.createService(RepairApi::class.java) }
    private val titleInput by lazy { findViewById<EditText>(R.id.add_repair_title) }
    private val descriptionInput by lazy { findViewById<EditText>(R.id.add_repair_description) }
    private val cancelButton by lazy { findViewById<Button>(R.id.add_repair_cancel) }
    private val submitButton by lazy { findViewById<Button>(R.id.add_repair_submit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_repair)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
        }

        submitButton.setOnClickListener {
            if (validateInputs()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = repairApi.reqRepairCreateRepairRequest(
                            ReqRepairCreateRepairRequest(
                                titleInput.text.toString(),
                                descriptionInput.text.toString(),
                                2
                            )
                        )

                        withContext(Dispatchers.Main) {
                            if (response.title.isNotEmpty()) {
                                Toast.makeText(this@AddRepairActivity, "Заявка создана", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AddRepairActivity, GeneralRepairsActivity::class.java))
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddRepairActivity, "Проблема с подключением, Код ошибки: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (titleInput.text.isEmpty()) {
            titleInput.error = "Заполните это поле"
            isValid = false
        }
        if (descriptionInput.text.isEmpty()) {
            descriptionInput.error = "Заполните это поле"
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }
}
