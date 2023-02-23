package com.example.hastkala.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.model.Profile
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Profile : AppCompatActivity() {
    companion object {
        private var ipaddress = "http://10.200.246.33:8081"

        fun get(ID: Int):Profile? {
            val url = URL("${ipaddress}/services/profile.php?id=${ID}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                setChunkedStreamingMode(0)
            }
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = JSONObject(reader.readText())
                    val profilejsonresponse = responseJsonString.getJSONObject("Profile")
                    return Profile(
                            profilejsonresponse.getString("Name"),
                            profilejsonresponse.getString("MobileNumber"),
                            profilejsonresponse.getString("Address"),
                            profilejsonresponse.getString("State"),
                            profilejsonresponse.getString("Gender"),
                            profilejsonresponse.getString("Pincode"),
                            profilejsonresponse.getString("Email")
                        )
                }
            } catch (ex: Exception) {
                Log.e("ERROR", ex.message.toString())
            }
            return null
        }

        fun saveprofile(
            ID: Int,
            Name: String,
            Mobileno: String,
            Address: String,
            State: String,
            Pincode: String,
            Gender: String
        ): JSONObject {
            val json = JSONObject()
            json.put("id", ID)
            json.put("Name", Name)
            json.put("Mobileno", Mobileno)
            json.put("Address", Address)
            json.put("State", State)
            json.put("Pincode", Pincode)
            json.put("Gender", Gender)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/profile.php")
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
            } catch (ex: java.lang.Exception) {
                Log.e("Save Profile ERROR", ex.message!!)
            }
            return null!!
        }

        fun updateprofile(
            ID: Int,
            Name: String,
            Mobileno: String,
            Address: String,
            State: String,
            Pincode: String,
            Gender: String
        ): JSONObject {
            val json = JSONObject()
            json.put("id", ID)
            json.put("Name", Name)
            json.put("Mobileno", Mobileno)
            json.put("Address", Address)
            json.put("State", State)
            json.put("Pincode", Pincode)
            json.put("Gender", Gender)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/profile.php")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "PUT"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                val writer = httpConnection.outputStream.bufferedWriter()
                writer.write(jsonResponse)
                writer.flush()
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = JSONObject(reader.readText())
                    return (responseJsonString)
                }
            } catch (ex: java.lang.Exception) {
                Log.e("Update Profile ERROR", ex.message!!)
            }
            return null!!
        }
    }
}