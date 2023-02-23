package com.example.hastkala.downloadimages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.hastkala.model.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Productsimg {
    companion object {
        private var ipaddress = "http://10.200.246.33:8081"
        fun downloadallproductimages(context: Context, product: Product) {

            val url = URL("${ipaddress}/services/${product.path}${product.image}")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            try {

                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/${product.path}"
                val imagepath = File(imageDirPath)

                if (!imagepath.exists()) {
                    imagepath.mkdirs()
                }

                val imageSavePath =
                    FileOutputStream("${imagepath.absolutePath}/${product.image}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, imageSavePath)
                }

            } catch (ex: Exception) {
                Log.e("ProductdownloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }

        fun downloadsingleproductimages(context: Context, productDetails: ProductDetails) {
            for (images in productDetails.images) {
                val url = URL("${ipaddress}/services/${productDetails.path}${images}")

                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    doOutput = true
                }

                try {

                    val cacheDirPath = context.externalCacheDir!!.absolutePath
                    val imageDirPath = "${cacheDirPath}/${productDetails.path}"
                    val imagepath = File(imageDirPath)

                    if (!imagepath.exists()) {
                        imagepath.mkdirs()
                    }

                    val imageSavePath =
                        FileOutputStream("${imagepath.absolutePath}/${images}")

                    connection.connect()
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageSavePath)
                    }

                } catch (ex: Exception) {
                    Log.e("ProductDetailsdownloadImage", ex.message!!)
                } finally {
                    connection.disconnect()
                }

            }
        }

        fun downloadcategoryimages(context: Context, category: Category) {

            val url = URL("${ipaddress}/services/${category.imagepath}${category.image}")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            try {

                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/${category.imagepath}"
                val imagepath = File(imageDirPath)

                if (!imagepath.exists()) {
                    imagepath.mkdirs()
                }

                val imageSavePath =
                    FileOutputStream("${imagepath.absolutePath}/${category.image}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, imageSavePath)
                }

            } catch (ex: Exception) {
                Log.e("CategorydownloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }

        fun downloadcartimg(context: Context, cart: Cart) {

            val url = URL("${ipaddress}/services/${cart.Imagepath}${cart.Image}")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            try {

                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/${cart.Imagepath}"
                val imagepath = File(imageDirPath)

                if (!imagepath.exists()) {
                    imagepath.mkdirs()
                }

                val imageSavePath =
                    FileOutputStream("${imagepath.absolutePath}/${cart.Image}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, imageSavePath)
                }

            } catch (ex: Exception) {
                Log.e("CartdownloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }

        fun downloadproceedtopayimg(context: Context, proceedtopay: Proceedtopay) {

            val url = URL("${ipaddress}/services/${proceedtopay.ImagesPath}${proceedtopay.Image}")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            try {

                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/${proceedtopay.ImagesPath}"
                val imagepath = File(imageDirPath)

                if (!imagepath.exists()) {
                    imagepath.mkdirs()
                }

                val imageSavePath =
                    FileOutputStream("${imagepath.absolutePath}/${proceedtopay.Image}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, imageSavePath)
                }

            } catch (ex: Exception) {
                Log.e("ProceedtopaydownloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }

        fun downloadorderimg(context: Context, order: Order) {

            val url = URL("${ipaddress}/services/${order.Imagepath}${order.Image}")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            try {

                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/${order.Imagepath}"
                val imagepath = File(imageDirPath)

                if (!imagepath.exists()) {
                    imagepath.mkdirs()
                }

                val imageSavePath =
                    FileOutputStream("${imagepath.absolutePath}/${order.Image}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, imageSavePath)
                }

            } catch (ex: Exception) {
                Log.e("OrderdownloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }
    }
}