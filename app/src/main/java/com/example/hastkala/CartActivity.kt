package com.example.hastkala

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.example.hastkala.api.Cart
import com.example.hastkala.downloadimages.Productsimg
import com.example.hastkala.adapters.CartGridAdapter
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var subprice: TextView
    private lateinit var grandTotal: TextView
    private lateinit var errormsg: TextView
    private lateinit var cardgrid: GridView
    private lateinit var btnproceedtopay: Button
    private lateinit var amountlayout: MaterialCardView
    private lateinit var btnshopping:Button
    private lateinit var emptycartimg:ImageView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.title = "Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cardgrid = findViewById(R.id.cartgrdview)
        subprice = findViewById(R.id.subprice)
        grandTotal = findViewById(R.id.grandtotal_price)
        btnproceedtopay = findViewById(R.id.proceed_to_checkout)
        amountlayout = findViewById(R.id.totallayout)
        errormsg = findViewById(R.id.error)
        btnshopping=findViewById(R.id.shopping)
        emptycartimg=findViewById(R.id.emptycartimg)
        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)

        CoroutineScope(Dispatchers.IO).launch {
            val cart = Cart.getcartproducts(userid)

            if (cart.isNotEmpty()) {
                val price = Cart.getFinalPrice(userid)
                val grandtotal = Cart.getFinalPrice(userid)

//                for (img in cart)
//                    Productsimg.downloadcartimg(this@CartActivity, img)

                withContext(Dispatchers.Main)
                {
                    subprice.text = "₹${price}"
                    grandTotal.text = "₹${grandtotal}"
                    val adapter = CartGridAdapter(this@CartActivity, cart)
                    cardgrid.adapter = adapter
                }
                amountlayout.visibility=View.VISIBLE
                btnproceedtopay.setOnClickListener {
                    val intent = Intent(this@CartActivity, ProceedtopayActivity::class.java)
                    startActivity(intent)
                    this@CartActivity.recreate()
                }
            } else {
                withContext(Dispatchers.Main) {
                    cardgrid.visibility = View.GONE
                    btnshopping.visibility=View.VISIBLE
                    emptycartimg.visibility=View.VISIBLE
                    btnshopping.setOnClickListener {
                        val intent=Intent(this@CartActivity,HomeActivity::class.java)
                        startActivity(intent)
                        finish()
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