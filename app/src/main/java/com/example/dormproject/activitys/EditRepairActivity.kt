package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
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
import com.example.dormproject.retrofit.req.repair.data.ReqRepairEditRepairByIdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRepairActivity : AppCompatActivity() {

    private val repairApi: RepairApi by lazy { ApiService.createService(RepairApi::class.java) }
    private val titleInput by lazy { findViewById<EditText>(R.id.edit_repair_title) }
    private val descriptionInput by lazy { findViewById<EditText>(R.id.edit_repair_description) }
    private val statusInput by lazy { findViewById<EditText>(R.id.edit_repair_status) }
    private val responsibleInput by lazy { findViewById<EditText>(R.id.edit_repair_responsible) }
    private val cancelButton by lazy { findViewById<Button>(R.id.edit_repair_cancel) }
    private val submitButton by lazy { findViewById<Button>(R.id.edit_repair_submit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_repair)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId = intent.getSerializableExtra("itemID") as? Int

        itemId?.let {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repair = repairApi.reqRepairGetRepairById(it)
                    withContext(Dispatchers.Main) {
                        titleInput.setText(repair.guestRequestList[0].title)
                        descriptionInput.setText(repair.guestRequestList[0].description)
                        statusInput.setText(repair.guestRequestList[0].statusId.toString())
                        responsibleInput.setText(repair.guestRequestList[0].responsible.toString())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditRepairActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
        }

        submitButton.setOnClickListener {
            if (itemId != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        repairApi.reqRepairEditGuestById(
                            itemId, ReqRepairEditRepairByIdRequest(
                                titleInput.text.toString(),
                                responsibleInput.text.toString().toInt(),
                                descriptionInput.text.toString(),
                                statusInput.text.toString().toInt()
                            )
                        )
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@EditRepairActivity, "Запись обновлена", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@EditRepairActivity, GeneralRepairsActivity::class.java))
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@EditRepairActivity, "Ошибка обновления записи", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
