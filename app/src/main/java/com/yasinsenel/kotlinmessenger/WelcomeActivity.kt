package com.yasinsenel.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomebutton_login.setOnClickListener {
           val intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)

        }

        welcomebutton_register.setOnClickListener {
            val intent = Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}