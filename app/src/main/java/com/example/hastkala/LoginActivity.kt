package com.example.hastkala

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.hastkala.api.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var btnsignup: Button
    private lateinit var btnlogin: Button
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        if (userid != 0) {
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        btnlogin = findViewById(R.id.LoginButton)

        btnlogin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val username = username.text.toString()
                val password = password.text.toString()
                val messege = User.login(username, password)
                withContext(Dispatchers.Main)
                {
                    if (!messege.getBoolean("Success")) {
                        Toast.makeText(
                            this@LoginActivity,
                            messege.getString("Messege"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
                        val prefeditor = pref.edit()
                        prefeditor.putInt("UserID", messege.getInt("ID"))
                        prefeditor.apply()
                        val intent = Intent(this@LoginActivity,HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        btnsignup = findViewById(R.id.SignUp)
        btnsignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}