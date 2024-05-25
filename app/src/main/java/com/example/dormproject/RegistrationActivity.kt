package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.retrofit.auth.AuthApi
import com.example.dormproject.retrofit.auth.AuthRegRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationActivity : AppCompatActivity(), ApiMain {

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinner = findViewById(R.id.reg_spinner_roleId)
        val listRolesItems = listOf("Работник", "Студент")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listRolesItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor()).addInterceptor(CookieInterceptor())
            .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val authApi = retrofit.create(AuthApi::class.java)

        val textInputLogin = findViewById<EditText>(R.id.reg_login_input)
        val textInputPassword = findViewById<EditText>(R.id.reg_password_input)
        val textInputRepeatPassword = findViewById<EditText>(R.id.reg_password_repeat_input)
        val textInputDormId = findViewById<EditText>(R.id.reg_dorm_id_input)

        val buttonToAuth = findViewById<Button>(R.id.register_to_auth)

        buttonToAuth.setOnClickListener {
            startActivity(
                Intent(
                    this, AutorizationActivity::class.java
                )
            )
        }

        val buttonReg = findViewById<Button>(R.id.registerButton)

        buttonReg.setOnClickListener {
            if (textInputLogin.text.toString().length < 5) {
                textInputLogin.error = "Логин должен быть больше 5 символов"
                return@setOnClickListener
            }
            if (textInputPassword.text.toString().length < 8) {
                textInputPassword.error = "Пароль должен быть больше 8 символов"
                return@setOnClickListener
            }
            if (textInputPassword.text.toString() != textInputRepeatPassword.text.toString()) {
                textInputPassword.error = "Пароли не совпадают"
                textInputRepeatPassword.error = "Пароли не совпадают"
                return@setOnClickListener
            }
            if (textInputDormId.text.toString().isEmpty()) {
                textInputDormId.error = "Укажите ID общежития"
                return@setOnClickListener
            }

            var roleId = 1

            if (spinner.selectedItem.toString() == "Работник") {
                roleId = 3
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val authResponse = authApi.authReg(
                        AuthRegRequest(
                            textInputLogin.text.toString(),
                            textInputPassword.text.toString(),
                            textInputDormId.text.toString().toInt(),
                            roleId
                        )
                    )

                    runOnUiThread {
                        if (authResponse.accessToken.isNotEmpty()) {
                            startActivity(
                                Intent(
                                    this@RegistrationActivity, GeneralRepairsActivity::class.java
                                )
                            )
                        }
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        val newE = e.toString().replace("java.io.IOException: ", "")
                        Toast.makeText(
                            this@RegistrationActivity,
                            "Неправильный логин или пароль, Код ошибки: $newE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}