package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dormproject.ApiService
import com.example.dormproject.R
import com.example.dormproject.retrofit.auth.AuthApi
import com.example.dormproject.retrofit.auth.AuthLoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AutorizationActivity : AppCompatActivity() {

    private val authApi: AuthApi by lazy { ApiService.createService(AuthApi::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorization)

        val loginButton: Button by lazy { findViewById<Button>(R.id.loginButton) }
        val registrationButton: Button by lazy { findViewById<Button>(R.id.register_to_auth) }
        val login: EditText by lazy { findViewById<EditText>(R.id.auth_input_login) }
        val password: EditText by lazy { findViewById<EditText>(R.id.auth_input_password) }

        loginButton.setOnClickListener {
            if (login.text.toString().length < 5) {
                login.error = "Логин должен быть больше 5 символов"
                return@setOnClickListener
            }
            if (password.text.toString().length < 8) {
                password.error = "Пароль должен быть больше 8 символов"
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = authApi.authLogin(AuthLoginRequest(login.text.toString(), password.text.toString()))
                    withContext(Dispatchers.Main) {
                        if (response.accessToken.isNotEmpty()) {
                            startActivity(Intent(this@AutorizationActivity, GeneralRepairsActivity::class.java))
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        val newE = e.toString().replace("java.io.IOException: ", "")
                        Toast.makeText(this@AutorizationActivity, "Неправильный логин или пароль, Код ошибки: $newE", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        registrationButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
