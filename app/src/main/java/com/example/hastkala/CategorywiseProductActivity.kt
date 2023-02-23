package com.example.hastkala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import com.example.hastkala.adapters.ProductGridAdapter
import com.example.hastkala.api.Product
import com.example.hastkala.downloadimages.Productsimg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategorywiseProductActivity : AppCompatActivity() {
    private lateinit var productgrid:GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorywise_product)
        val name=intent.getStringExtra("Catname")
        supportActionBar?.title=name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        productgrid=findViewById(R.id.category_gridview)
        CoroutineScope(Dispatchers.IO).launch {
            val products=Product.getAll()
            if (products.isNotEmpty()) {
//                for (img in products)
//                    Productsimg.downloadallproductimages(this@CategorywiseProductActivity, img)

                withContext(Dispatchers.Main) {
                    productgrid.adapter = ProductGridAdapter(this@CategorywiseProductActivity, products)
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