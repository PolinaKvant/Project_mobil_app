package com.example.dormproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authButton = findViewById<Button>(R.id.login)
        val registerButton = findViewById<Button>(R.id.register)

        authButton.setOnClickListener {
            startActivity(Intent(
                this,
                AutorizationActivity::class.java
            ))
        }

        registerButton.setOnClickListener {
            startActivity(Intent(
                this,
                RegistrationActivity::class.java
            ))
        }

    }
}