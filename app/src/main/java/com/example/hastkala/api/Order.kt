package com.example.hastkala.api

import android.util.Log
import com.example.hastkala.model.Order
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Order {
    companion object {
        private var ipaddress = "http://10.200.246.33:8081"
        fun getorder(Userid: Int): Array<Order> {
            val orderlist = arrayListOf<Order>()
            val url = URL("${ipaddress}/services/order.php?Userid=${Userid}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                setChunkedStreamingMode(0)
            }
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJsonString = JSONObject(reader.readText())
                    if (responseJsonString.getBoolean("Success")) {
                        var i = 0
                        val orderjsonresponse = responseJsonString.getJSONArray("Orders")
                        while (i < orderjsonresponse.length()) {
                            val ordered = orderjsonresponse.getJSONObject(i)
                            with(ordered)
                            {
                                val order = Order(
                                    getInt("OrderID"),
                                    getInt("ProductId"),
                                    getString("SizeID"),
                                    getString("Name"),
                                    getString("Details"),
                                    getString("Quantity"),
                                    getString("TotalPrice"),
                                    getString("Price"),
                                    getString("Imagepath"),
                                    getString("Image"),
                                    getString("Status"),
                                    getString("Sizes")
                                )
                                orderlist.add(order)
                            }
                            i++
                        }
                    }else{
                        orderlist.clear()
                    }
                }
            } catch (ex: Exception) {
                Log.e("ERROR", ex.message.toString())
            }
            return orderlist.toTypedArray()
        }

        fun singleorderrequest(
            purchaseqty: Any?,
            productid: Int,
            totalprice: String,
            sizeid: Any?,
            Userid: Int
        ): JSONObject {
            val json = JSONObject()
            json.put("purchaseqty", purchaseqty)
            json.put("productid", productid)
            json.put("totalprice", totalprice)
            json.put("sizeid", sizeid)
            json.put("userid", Userid)

            val responsejson = json.toString()
            val url = URL("${ipaddress}/services/order.php")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                val writer = httpConnection.outputStream.bufferedWriter()
                writer.write(responsejson)
                writer.flush()
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    return JSONObject(reader.readText())
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return null!!
        }

        fun allproductrequest(Userid: Int): JSONObject {
            val json = JSONObject()
            json.put("Userid", Userid)
            val responsejson = json.toString()
            val url = URL("${ipaddress}/services/order.php")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "PUT"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                val writer = httpConnection.outputStream.bufferedWriter()
                writer.write(responsejson)
                writer.flush()
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJson = reader.readText()
                    return JSONObject(responseJson)
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return null!!
        }

        fun deletefromorder(
            productid: Int,
            Orderid: Int,
            Userid: Int,
            purchaseqty: String
        ): JSONObject {
            val json = JSONObject()
            json.put("productid", productid)
            json.put("orderid", Orderid)
            json.put("userid", Userid)
            json.put("purchaseqty", purchaseqty)
            val responsejson = json.toString()
            val url = URL("${ipaddress}/services/order.php")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                doOutput = true
                requestMethod = "DELETE"
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            try {
                val writer = httpConnection.outputStream.bufferedWriter()
                writer.write(responsejson)
                writer.flush()
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpConnection.inputStream.bufferedReader()
                    val responseJson = reader.readText()
                    return JSONObject(responseJson)
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return null!!
        }
    }
}