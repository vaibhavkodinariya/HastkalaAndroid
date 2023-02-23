package com.example.hastkala.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.model.Cart
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Cart : AppCompatActivity() {
    companion object {
        private var ipaddress = "http://10.200.246.33:8081"

        fun insertincart(
            sizeid: String,
            quantity: String,
            productid: Int,
            userid: Int,
        ): JSONObject {
            val json = JSONObject()

            json.put("Sizeid", sizeid)
            json.put("Quantity", quantity)
            json.put("Productid", productid)
            json.put("UserId", userid)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/cart.php")
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
                Log.e("Cart Insert", ex.message!!)
            }
            return null!!
        }

        fun updateincart(
            quantity: Int,
            productid: Int,
            userid: Int,
            orderid: Int
        ): JSONObject {
            val json = JSONObject()

            json.put("quantity", quantity)
            json.put("productid", productid)
            json.put("userid", userid)
            json.put("orderid", orderid)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/cart.php")
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
                    val responseJsonString = reader.readText()
                    return JSONObject(responseJsonString)
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return null!!
        }

        fun deletefromcart(orderid: Int, userid: Int): JSONObject {
            val json = JSONObject()

            json.put("orderid", orderid)
            json.put("userid", userid)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/cart.php")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "DELETE"
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
                Log.e("Delete from cart", ex.message!!)
            }
            return null!!
        }

        fun getcartproducts(userid: Int): Array<Cart> {
            val cartProducts = arrayListOf<Cart>()
            val url = URL("${ipaddress}/services/cart.php?id=${userid}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = JSONObject(reader.readText())
                    val cartjsonresponse = responseJsonString.getJSONArray("Cart")
                    if (responseJsonString.getBoolean("Success")) {
                        var i = 0
                        while (i < cartjsonresponse.length()) {
                            val cart = cartjsonresponse.getJSONObject(i)
                            val cartproducts = Cart(
                                cart.getInt("OrderID"),
                                cart.getInt("ProductId"),
                                cart.getString("Quantity"),
                                cart.getString("TotalPrice"),
                                cart.getString("SizeID"),
                                cart.getString("Name"),
                                cart.getString("Price"),
                                cart.getString("Details"),
                                cart.getString("ImagePath"),
                                cart.getString("Image"),
                                cart.getString("Sizes")
                            )
                            cartProducts.add(cartproducts)
                            i++
                        }
                    } else {
                        cartProducts.clear()
                    }
                }
            } catch (ex: Exception) {
                Log.e("Fetch cart", ex.message!!)
            }
            return cartProducts.toTypedArray()
        }

        fun getFinalPrice(userid: Int): String {
            val url = URL("${ipaddress}/services/cart.php?id=${userid}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = JSONObject(reader.readText())
                    return responseJsonString.getString("FinalPrice")

                }
            } catch (ex: Exception) {
                Log.e("Fetch cart", ex.message!!)
            }
            return null!!
        }
    }
}