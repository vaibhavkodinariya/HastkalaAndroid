package com.example.hastkala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.api.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {
    private lateinit var btnregister: Button
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmpassword: TextInputEditText
    private lateinit var btnlogin:Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmpassword = findViewById(R.id.confirmpsd)
        if(isValidEmail(email.toString()))
        {
            email.error=null

        }
        else
        {
            email.error="Email can not be Empty!!!"
        }
        btnregister = findViewById(R.id.register)
        btnregister.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val email = email.text.toString()
                val password = password.text.toString()
                val confirmpassword = confirmpassword.text.toString()
                val messege = User.register(email, password, confirmpassword)
                withContext(Dispatchers.Main)
                {
                    if (!messege.getBoolean("Success")) {
                        Toast.makeText(
                            this@RegisterActivity,
                            messege.getString("Messege"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent=Intent(this@RegisterActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        btnlogin=findViewById(R.id.AlreadyUser)
        btnlogin.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    val EmailAddress: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && EmailAddress.matcher(target).matches()
    }

}