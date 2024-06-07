package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.dormproject.ApiService
import com.example.dormproject.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ApiService с контекстом текущего приложения
        ApiService.initialize(this)

        // Установка макета для данной активности
        setContentView(R.layout.activity_main)

        // Проверка, авторизован ли пользователь
        if (ApiService.isUserLoggedIn()) {
            // Если пользователь авторизован, перенаправляем его на активность GeneralRepairsActivity
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
            finish() // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
            return
        }

        // Ленивая инициализация кнопок входа и регистрации
        val authButton: Button by lazy { findViewById<Button>(R.id.login) }
        val registerButton: Button by lazy { findViewById<Button>(R.id.register) }

        // Установка обработчика нажатия для кнопки входа
        authButton.setOnClickListener {
            startActivity(Intent(this, AutorizationActivity::class.java))
        }

        // Установка обработчика нажатия для кнопки регистрации
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
