package com.example.hastkala

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.adapters.ProceedtopayGridAdapter
import com.example.hastkala.api.Order
import com.example.hastkala.api.Proceedtopay
import com.example.hastkala.api.Profile
import com.example.hastkala.downloadimages.Productsimg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProceedtopayActivity : AppCompatActivity() {
    private lateinit var gridview: GridView
    private lateinit var subtotal: TextView
    private lateinit var grandtotal: TextView
    private lateinit var btnproceedtopay: Button
    private lateinit var name: TextView
    private lateinit var mobileno: TextView
    private lateinit var address: TextView
    private lateinit var edit:Button

    private var code:Int = 0
    private var quantity:Any?=null
    private var productid:Int=0
    private var sizeid:Any?=null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proceedtopay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gridview = findViewById(R.id.ProceedToPaygrdview)
        subtotal = findViewById(R.id.subprice)
        grandtotal = findViewById(R.id.grandtotal_price)
        btnproceedtopay = findViewById(R.id.proceed_to_checkout)
        name = findViewById(R.id.name_value)
        mobileno = findViewById(R.id.mobilenumber_value)
        address = findViewById(R.id.address_value)
        edit=findViewById(R.id.edit_details)
        edit.setOnClickListener {
            val intent=Intent(this,ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val profile = Profile.get(userid)
            if (profile != null) {
                withContext(Dispatchers.Main)
                {
                    name.text = profile.Name
                    mobileno.text = profile.MobileNumber
                    address.text = profile.Address
                }
            }
        }
        if(intent.hasExtra("Code") && intent.hasExtra("quantity") && intent.hasExtra("productid") && intent.hasExtra("sizeid"))
        {
            code = intent.getIntExtra("Code", 0)
            quantity = intent.extras!!["quantity"]
            productid = intent.getIntExtra("productid", 0)
            sizeid = intent.extras!!["sizeid"]
            if (code == 1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val totalprice =
                        Proceedtopay.getTotalPriceofproduct(quantity, productid, sizeid)
                    Proceedtopay.singlerequest(quantity, productid, sizeid).also {
                        for (img in it)
                            Productsimg.downloadproceedtopayimg(this@ProceedtopayActivity, img)
                        val adapter =
                            ProceedtopayGridAdapter(this@ProceedtopayActivity, it)
                        withContext(Dispatchers.Main) {
                            gridview.adapter = adapter
                            grandtotal.text = totalprice
                            subtotal.text = totalprice
                        }
                    }
                }
            }
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                val proceedtopayproducts = Proceedtopay.allproductrequest(userid)
                val subPrice = Proceedtopay.getFinalPrice(userid)
                val grandTotal = Proceedtopay.getFinalPrice(userid)
                if (proceedtopayproducts.isNotEmpty()) {
                    for (img in proceedtopayproducts)
                        Productsimg.downloadproceedtopayimg(this@ProceedtopayActivity, img)
                    val adapter =
                        ProceedtopayGridAdapter(this@ProceedtopayActivity, proceedtopayproducts)
                    withContext(Dispatchers.Main) {
                        subtotal.text = "₹${subPrice}"
                        grandtotal.text = "₹${grandTotal}"
                        gridview.adapter = adapter
                    }
                }
            }
        }
        btnproceedtopay.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if(code==1)
                {
                    val messege=Order.singleorderrequest(quantity,productid,grandtotal.text.toString(),sizeid,userid)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProceedtopayActivity,
                            messege.getString("Messege"),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@ProceedtopayActivity, OrderActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                else
                {
                    val messege = Order.allproductrequest(userid)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProceedtopayActivity,
                            messege.getString("Messege"),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@ProceedtopayActivity, OrderActivity::class.java)
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