package com.example.dormproject.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.dormproject.ApiService
import com.example.dormproject.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiService.initialize(this)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "User is logged in: ${ApiService.isUserLoggedIn()}")

        if (ApiService.isUserLoggedIn()) {
            Log.d("MainActivity", "Redirecting to GeneralRepairsActivity")
            startActivity(Intent(this, GeneralRepairsActivity::class.java))
            finish()
            return
        }

        val authButton: Button by lazy { findViewById<Button>(R.id.login) }
        val registerButton: Button by lazy { findViewById<Button>(R.id.register) }

        authButton.setOnClickListener {
            startActivity(Intent(this, AutorizationActivity::class.java))
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
