package com.example.hastkala

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.adapters.DropDownAdapter
import com.example.hastkala.api.Cart
import com.example.hastkala.api.Product
import com.example.hastkala.downloadimages.Productsimg
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.synnapps.carouselview.CarouselView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductActivity : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var details: TextView
    private lateinit var discountedprice: TextView
    private lateinit var actualprice: TextView
    private lateinit var savedprice: TextView
    private lateinit var sizecard: MaterialCardView
    private lateinit var sizespinner: AutoCompleteTextView
    private lateinit var qtyspinner: AutoCompleteTextView

    private lateinit var btnbuy: Button
    private lateinit var btncart:Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productid = intent.getIntExtra("productid", 0)

        name = findViewById(R.id.Product_name)
        details = findViewById(R.id.ProductDiscription)
        discountedprice = findViewById(R.id.product_discount)
        actualprice = findViewById(R.id.discounted_price)
        savedprice = findViewById(R.id.discount)
        sizecard = findViewById(R.id.size_card)
        sizespinner = findViewById(R.id.autocomplete_size)
        qtyspinner = findViewById(R.id.autocomplete_quantity)

        btnbuy = findViewById(R.id.Btn_buy)
        btncart=findViewById(R.id.Btn_AddCart)
        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        btncart.isEnabled=false
        btnbuy.isEnabled=false
        val quantity = resources.getStringArray(R.array.Quantity)
        val adapterquantity =
            ArrayAdapter(this@ProductActivity, R.layout.quantitydropdown, quantity)
        qtyspinner.setAdapter(adapterquantity)
        qtyspinner.setText(adapterquantity.getItem(0), false)

        CoroutineScope(Dispatchers.IO).launch {
            Product.getProductDetails(productid).also {
                if (it != null) {
//                    Productsimg.downloadsingleproductimages(this@ProductActivity, it)

                    withContext(Dispatchers.Main) {
                        val carouselView = findViewById<CarouselView>(R.id.carouselView)
                        carouselView.setImageListener { position, imageView ->
                            imageView.setImageURI(Uri.parse("${externalCacheDir?.absolutePath}/${it.path}${it.images[position]}"))
                        }
                        carouselView.pageCount = it.images.size
                        supportActionBar?.title = it.name
                        name.text = it.name
                        details.text = it.details
                        val percentageprice = it.price * 25 / 100
                        val discountprice = it.price + percentageprice
                        discountedprice.text = "₹$discountprice"
                        actualprice.text = "₹${it.price}"
                        savedprice.text = "You Save ₹${(discountprice - it.price)}"
                        val size = arrayListOf<String>()
                        if (it.sizes.isEmpty()) {
                            sizecard.visibility = View.GONE
                            btncart.isEnabled=true
                            btnbuy.isEnabled=true
                            size.clear()
                        } else {
                            val sizeadapter = DropDownAdapter(this@ProductActivity, it.sizes)
                            sizespinner.setAdapter(sizeadapter)
                            sizespinner.setOnItemClickListener { _, _, i, _ ->
                                size.add(it.sizes[i].sizeId.toString())
                                btncart.isEnabled=true
                                btnbuy.isEnabled=true
                            }
                        }
                        btnbuy.setOnClickListener {
                            if (userid == 0) {
                                MaterialAlertDialogBuilder(this@ProductActivity)
                                    .setTitle("Log In First To Buy This Products")
                                    .setNeutralButton("cancel") { _, _ ->
                                        closeContextMenu()
                                    }
                                    .setPositiveButton("Login") { _, _ ->
                                        val intent =
                                            Intent(this@ProductActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .show()
                            } else {
                                val sizeid = if (size.isEmpty()) {
                                    0
                                } else {
                                    size.last()
                                }
                                val intent =
                                    Intent(this@ProductActivity, ProceedtopayActivity::class.java)
                                intent.putExtra("Code", 1)
                                intent.putExtra("quantity", qtyspinner.text)
                                intent.putExtra("sizeid", sizeid)
                                intent.putExtra("productid", productid)
                                startActivity(intent)
                            }
                        }
                        btncart.setOnClickListener{
                            if (userid == 0) {
                                MaterialAlertDialogBuilder(this@ProductActivity)
                                    .setTitle("Log In First To Add To Cart This Products")
                                    .setNeutralButton("cancel") { _, _ ->
                                        closeContextMenu()
                                    }
                                    .setPositiveButton("Login") { _, _ ->
                                        val intent =
                                            Intent(this@ProductActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .show()
                            }else{
                                CoroutineScope(Dispatchers.IO).launch {
                                    val sizeid = if (size.isEmpty()) {
                                        0
                                    } else {
                                        size.last()
                                    }
                                    val message = Cart.insertincart(sizeid.toString(),qtyspinner.text.toString(),productid,userid)
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            this@ProductActivity,
                                            message.getString("Messege"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        this@ProductActivity.recreate()
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home)
            finish()

        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        return super.onOptionsItemSelected(item)
    }
}