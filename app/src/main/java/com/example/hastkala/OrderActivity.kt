package com.example.hastkala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import com.example.hastkala.adapters.OrderGridAdapter
import com.example.hastkala.adapters.ProceedtopayGridAdapter
import com.example.hastkala.api.Order
import com.example.hastkala.api.Proceedtopay
import com.example.hastkala.downloadimages.Productsimg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderActivity : AppCompatActivity() {
    private lateinit var ordergrd: GridView
    private lateinit var errorinfo: TextView
    private lateinit var btnshopping: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.title = "Orders"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ordergrd = findViewById(R.id.ordergrdview)
        errorinfo = findViewById(R.id.error)
        btnshopping = findViewById(R.id.shopping)
        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)

        CoroutineScope(Dispatchers.IO).launch {
            val orderproducts = Order.getorder(userid)
            if (orderproducts.isNotEmpty()) {
                for (img in orderproducts)
                    Productsimg.downloadorderimg(this@OrderActivity, img)

                val adapter = OrderGridAdapter(this@OrderActivity, orderproducts)
                withContext(Dispatchers.Main) { ordergrd.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    ordergrd.visibility=View.GONE
                    errorinfo.visibility = View.VISIBLE
                    btnshopping.visibility = View.VISIBLE
                    errorinfo.text = getString(R.string.orderempty)
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