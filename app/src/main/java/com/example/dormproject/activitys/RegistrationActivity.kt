package com.example.dormproject.activitys

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
import com.example.dormproject.R
import com.example.dormproject.ApiService
import com.example.dormproject.retrofit.auth.AuthApi
import com.example.dormproject.retrofit.auth.AuthRegRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {

    // Инициализация API для работы с авторизацией и регистрацией
    private val authApi: AuthApi by lazy { ApiService.createService(AuthApi::class.java) }

    // Инициализация полей ввода и кнопок
    private val textInputLogin: EditText by lazy { findViewById<EditText>(R.id.reg_login_input) }
    private val textInputPassword: EditText by lazy { findViewById<EditText>(R.id.reg_password_input) }
    private val textInputRepeatPassword: EditText by lazy { findViewById<EditText>(R.id.reg_password_repeat_input) }
    private val textInputDormId: EditText by lazy { findViewById<EditText>(R.id.reg_dorm_id_input) }
    private val buttonToAuth: Button by lazy { findViewById<Button>(R.id.register_to_auth) }
    private val buttonReg: Button by lazy { findViewById<Button>(R.id.registerButton) }
    private val spinner: Spinner by lazy { findViewById<Spinner>(R.id.reg_spinner_roleId) }

    // Метод onCreate вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Включение поддержки edge-to-edge отображения
        enableEdgeToEdge()

        // Установка макета для активности
        setContentView(R.layout.activity_registration)

        // Обработка системных отступов для корректного отображения контента
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Настройка спиннера с ролями
        setupSpinner()

        // Настройка обработчиков событий для кнопок
        setupListeners()
    }

    // Настройка спиннера для выбора роли
    private fun setupSpinner() {
        val listRolesItems = listOf("Работник", "Студент")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listRolesItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
    }

    // Настройка обработчиков нажатий для кнопок
    private fun setupListeners() {
        // Переход на экран авторизации при нажатии на кнопку "Уже есть аккаунт?"
        buttonToAuth.setOnClickListener {
            startActivity(Intent(this, AutorizationActivity::class.java))
        }

        // Проверка и регистрация пользователя при нажатии на кнопку "Зарегистрироваться"
        buttonReg.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }
    }

    // Проверка корректности введенных данных
    private fun validateInputs(): Boolean {
        if (textInputLogin.text.length < 5) {
            textInputLogin.error = "Логин должен быть больше 5 символов"
            return false
        }
        if (textInputPassword.text.length < 8) {
            textInputPassword.error = "Пароль должен быть больше 8 символов"
            return false
        }
        if (textInputPassword.text.toString() != textInputRepeatPassword.text.toString()) {
            textInputPassword.error = "Пароли не совпадают"
            textInputRepeatPassword.error = "Пароли не совпадают"
            return false
        }
        if (textInputDormId.text.isEmpty()) {
            textInputDormId.error = "Укажите ID общежития"
            return false
        }
        return true
    }

    // Регистрация пользователя с использованием API
    private fun registerUser() {
        val roleId = if (spinner.selectedItem.toString() == "Работник") 3 else 1

        // Запуск корутины для выполнения сетевого запроса
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Выполнение запроса на регистрацию
                val authResponse = authApi.authReg(
                    AuthRegRequest(
                        textInputLogin.text.toString(),
                        textInputPassword.text.toString(),
                        textInputDormId.text.toString().toInt(),
                        roleId
                    )
                )
                // Если регистрация успешна, переход на экран GeneralRepairsActivity
                if (authResponse.accessToken.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@RegistrationActivity, GeneralRepairsActivity::class.java))
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибки регистрации
                withContext(Dispatchers.Main) {
                    val newE = e.toString().replace("java.io.IOException: ", "")
                    Toast.makeText(this@RegistrationActivity, "Неправильный логин или пароль, Код ошибки: $newE", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}