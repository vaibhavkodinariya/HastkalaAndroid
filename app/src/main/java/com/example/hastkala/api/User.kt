package com.example.hastkala.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class User : AppCompatActivity() {
    companion object {
        private var ipaddress="http://10.200.246.33:8081"
        fun login(email: String, password: String): JSONObject {
            val json = JSONObject()
            json.put("Email", email)
            json.put("Password", password)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/user.php/login")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
            }
            try {
                val writer = httpConnection.outputStream.bufferedWriter()
                writer.write(jsonResponse)
                writer.flush()
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = reader.readText()
                    return JSONObject(responseJsonString)
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return null!!
        }

        fun register(email: String, password: String, ConfirmpPassword: String): JSONObject {
            val json = JSONObject()

            json.put("email", email)
            json.put("password", password)
            json.put("confirmpassword", ConfirmpPassword)

            val jsonResponse = json.toString()

            val url = URL("${ipaddress}/services/user.php")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                val writer = httpConnection.outputStream.bufferedWriter()
                writer.write(jsonResponse)
                writer.flush()

                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = reader.readText()
                    return JSONObject(responseJsonString)
                }
            } catch (ex: Exception) {
                Log.e("Register", ex.message!!)
            }
            return null!!
        }
    }
}
