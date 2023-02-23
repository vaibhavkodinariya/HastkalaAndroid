package com.example.hastkala.api

import android.util.Log
import com.example.hastkala.model.Category
import com.example.hastkala.model.Product
import com.example.hastkala.model.ProductDetails
import com.example.hastkala.model.ProductSize
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Product {

    companion object {

        private var ipaddress = "http://10.200.246.33:8081"

        fun getAll(): Array<Product> {

            val productList = arrayListOf<Product>()

            val url = URL("${ipaddress}/services/product.php")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                setChunkedStreamingMode(0)
            }

            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = connection.inputStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())

                    val productsJson = responseJson.getJSONArray("products")

                    var i = 0
                    while (i < productsJson.length()) {
                        val productJson = productsJson.getJSONObject(i)
                        val product = Product(
                            productJson.getInt("ID"),
                            productJson.getString("Name"),
                            productJson.getString("Details"),
                            productJson.getInt("Price"),
                            productJson.getString("ImagesPath"),
                            productJson.getString("Image")
                        )
                        productList.add(product)
                        i++
                    }
                }
            } catch (ex: Exception) {
                Log.e("Error", ex.message!!)
            } finally {
                connection.disconnect()
            }
            return productList.toTypedArray()
        }

        fun getProductsForGender(isMale: Boolean): Array<Product> {
            val productList = arrayListOf<Product>()
            val url = URL("${ipaddress}/services/product.php/${if (isMale) "Male" else "Female"}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                setChunkedStreamingMode(0)
            }
            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {

                    val reader = connection.inputStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())
                    val productsJson = responseJson.getJSONArray("Products")

                    var i = 0

                    while (i < productsJson.length()) {
                        val productJson = productsJson.getJSONObject(i)
                        val product = Product(
                            productJson.getInt("ID"),
                            productJson.getString("Name"),
                            productJson.getString("Details"),
                            productJson.getInt("Price"),
                            productJson.getString("ImagesPath"),
                            productJson.getString("images")
                        )
                        productList.add(product)
                        i++
                    }
                }
            } catch (ex: Exception) {
                Log.e("Error", ex.message!!)
            } finally {
                connection.disconnect()
            }
            return productList.toTypedArray()
        }

        fun getCategories(): Array<Category> {

            val categoryList = arrayListOf<Category>()

            val url = URL("${ipaddress}/services/product.php/Category")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                setChunkedStreamingMode(0)
            }

            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = connection.inputStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())
                    val productsJson = responseJson.getJSONArray("Category")

                    var i = 0

                    while (i < productsJson.length()) {
                        val productJson = productsJson.getJSONObject(i)
                        val category = Category(
                            productJson.getInt("ID"),
                            productJson.getString("Name"),
                            productJson.getString("ImagesPath"),
                            productJson.getString("images")
                        )
                        i++

                        categoryList.add(category)
                    }
                }
            } catch (ex: Exception) {
                Log.e("Error", ex.message!!)
            } finally {
                connection.disconnect()
            }
            return categoryList.toTypedArray()
        }

        fun getProductDetails(ID: Int): ProductDetails? {

            val url = URL("${ipaddress}/services/product.php/${ID}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                setChunkedStreamingMode(0)
            }
            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {

                    val reader = connection.inputStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())
                    val productSizes = arrayListOf<ProductSize>()
                    val productDetailsJson = responseJson.getJSONObject("Product")

                    val imagesJsonArray = productDetailsJson.getJSONArray("images")

                    if (productDetailsJson.getString("sizes").equals("false")){
                        productSizes.clear()
                    }
                    else
                    {
                        val sizesJsonArray = productDetailsJson.getJSONArray("sizes")
                        var index = 0
                        while (index < sizesJsonArray.length()) {
                            with(sizesJsonArray[index] as JSONObject) {
                                productSizes.add(
                                    ProductSize(
                                        getString("fixedSize"),
                                        getInt("sizeId")
                                    )
                                )
                            }
                            index++
                        }
                    }

                    var index = 0
                    val productImages = arrayListOf<String>()
                    while (index < imagesJsonArray.length()) {
                        productImages.add(imagesJsonArray[index].toString())
                        index++
                    }
                    with(productDetailsJson)
                    {
                        return ProductDetails(
                            getInt("ID"),
                            getString("Name"),
                            getString("Details"),
                            getInt("Price"),
                            getString("Color"),
                            getString("Material"),
                            getString("Composition"),
                            getString("ImagesPath"),
                            productImages.toTypedArray(),
                            productSizes.toTypedArray()
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("Error", ex.message!!)
            } finally {
                connection.disconnect()
            }
            return null!!
        }
    }
}