package com.example.hastkala.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.model.Proceedtopay
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Proceedtopay : AppCompatActivity() {
    companion object {
        private var ipaddress = "http://10.200.246.33:8081"

        fun singlerequest(
            quantity: Any?,
            productid: Int,
            sizeid: Any?,
        ): Array<Proceedtopay> {
            val json = JSONObject()
            json.put("quantity", quantity)
            json.put("productid", productid)
            json.put("sizeid", sizeid)

            val jsonResponse = json.toString()
            val productlist = arrayListOf<Proceedtopay>()
            val url = URL("${ipaddress}/services/proceedtopay.php/Buy")
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
                    val responseJsonString = JSONObject(reader.readText())
                    val proceedtopayjsonresponse = responseJsonString.getJSONArray("Orderdproducts")
                    var i = 0
                    while (i < proceedtopayjsonresponse.length()) {
                        val orderd = proceedtopayjsonresponse.getJSONObject(i)
                        with(orderd)
                        {
                            val orderedproduct = Proceedtopay(
                                null,
                                null,
                                getInt("ID"),
                                getString("Name"),
                                getString("Details"),
                                getString("Price"),
                                getString("Purchaseqty"),
                                getString("ImagesPath"),
                                getString("Image"),
                                getString("TotalPrice"),
                                null,
                                if (getString("Size") == "false") {
                                    "false"
                                } else {
                                    getString("Size")
                                }
                            )
                            productlist.add(orderedproduct)
                        }
                        i++
                    }
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return productlist.toTypedArray()
        }

        fun getTotalPriceofproduct(
            quantity: Any?,
            productid: Int,
            sizeid: Any?
        ): String {
            val json = JSONObject()
            json.put("quantity", quantity)
            json.put("productid", productid)
            json.put("sizeid", sizeid)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/proceedtopay.php/Buy")
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
                    val responseJsonString = JSONObject(reader.readText())
                    return responseJsonString.getJSONArray("Orderdproducts").getJSONObject(0)
                        .getString("TotalPrice")
                }
            } catch (ex: Exception) {
                Log.e("Fetch cart", ex.message!!)
            }
            return null!!
        }

        fun allproductrequest(userid: Int): Array<Proceedtopay> {
            val productlist = arrayListOf<Proceedtopay>()
            val json = JSONObject()
            json.put("userid", userid)

            val jsonResponse = json.toString()
            val url = URL("${ipaddress}/services/proceedtopay.php")
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
                    val responseJsonString = JSONObject(reader.readText())
                    val orderdproductsjson = responseJsonString.getJSONArray("Orderdproducts")
                    var i = 0
                    while (i < orderdproductsjson.length()) {
                        val orderd = orderdproductsjson.getJSONObject(i)
                        with(orderd)
                        {
                            val orderdproducts = Proceedtopay(
                                getInt("OrderID"),
                                getString("DateTime"),
                                getInt("ProductId"),
                                getString("Name"),
                                getString("Detail"),
                                getString("Price"),
                                getString("Quantity"),
                                getString("Imagepath"),
                                getString("Image"),
                                getString("TotalPrice"),
                                if (getString("SizeID") == "null") {
                                    null
                                } else {
                                    getString("SizeID")
                                },
                                if (getString("Size") == "false") {
                                    "false"
                                } else {
                                    getString("Size")
                                }
                            )
                            productlist.add(orderdproducts)
                        }
                        i++
                    }
                }
            } catch (ex: Exception) {
                Log.e("Login", ex.message!!)
            }
            return productlist.toTypedArray()
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