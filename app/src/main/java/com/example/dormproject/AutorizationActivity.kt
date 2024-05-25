package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormproject.retrofit.auth.AuthApi
import com.example.dormproject.retrofit.auth.AuthLoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AutorizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_autorization)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registerButton = findViewById<Button>(R.id.register_to_auth)

        registerButton.setOnClickListener {
            val newIntent = Intent(this@AutorizationActivity, RegistrationActivity::class.java)
            startActivity(newIntent)
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(ResponseCheckInterceptor()).addInterceptor(CookieInterceptor())
            .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dorma.virusbeats.ru/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()



        val authApi = retrofit.create(AuthApi::class.java)

        val loginButton = findViewById<Button>(R.id.loginButton)

        val login = findViewById<TextView>(R.id.auth_input_login)
        val password = findViewById<TextView>(R.id.auth_input_password)

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
                    val response = authApi.authLogin(
                        AuthLoginRequest(
                            login.text.toString(), password.text.toString()
                        )
                    )
                    runOnUiThread {
                        if (response.accessToken.isNotEmpty()) {
                            startActivity(
                                Intent(
                                    this@AutorizationActivity, GeneralRepairsActivity::class.java
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        val newE = e.toString().replace("java.io.IOException: ", "")
                        Toast.makeText(
                            this@AutorizationActivity,
                            "Неправильный логин или пароль, Код ошибки: $newE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}