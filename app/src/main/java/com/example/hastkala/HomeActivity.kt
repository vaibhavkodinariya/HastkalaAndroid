package com.example.hastkala

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hastkala.adapters.CategoryGridAdapter
import com.example.hastkala.adapters.ProductGridAdapter
import com.example.hastkala.api.Product
import com.example.hastkala.api.Profile
import com.example.hastkala.downloadimages.Productsimg
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifImageView


class HomeActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var categorygrid: GridView
    private lateinit var maletab: GridView
    private lateinit var femaletab: GridView
    private lateinit var productgrid:GridView
    private var sampleImages = intArrayOf(
        R.drawable.buddha,
        R.drawable.banner1,
        R.drawable.banner3,
        R.drawable.banner4,
        R.drawable.banner5,
        R.drawable.banner7,
        R.drawable.banner8
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        categorygrid = findViewById(R.id.category_gridview)
        maletab = findViewById(R.id.MaleTab)
        femaletab = findViewById(R.id.FemaleTab)
        productgrid=findViewById(R.id.Product_grid)
        drawer = findViewById(R.id.drawer)

        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        Log.i("UserID",userid.toString())

        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        carouselView.setImageListener(imageListener)
        carouselView.pageCount = sampleImages.size

        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        drawer.addDrawerListener(drawerToggle)


        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.NavView)
        val menu = navigationView.menu
        val header=navigationView.getHeaderView(0)

        val malegif = header.findViewById<GifImageView>(R.id.malegif)
        val femalegif = header.findViewById<GifImageView>(R.id.femalegif)
        val email=header.findViewById<TextView>(R.id.email)
        val name=header.findViewById<TextView>(R.id.name)
        val errorinfo=header.findViewById<TextView>(R.id.errorinfo)
        if (userid==0)
        {
            email.visibility=View.GONE
            name.visibility=View.GONE
            errorinfo.visibility=View.VISIBLE
        }
        val logout = menu.findItem(R.id.menu_logout)
        logout.isVisible = userid != 0

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    val homeintent = Intent(this, HomeActivity::class.java)
                    drawer.closeDrawer(GravityCompat.START)
                    startActivity(homeintent)
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                }

                R.id.menu_account -> {
                    if (userid == 0) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Log In To See Your Account")
                            .setNeutralButton("cancel") { _, _ ->
                                closeContextMenu()
                            }
                            .setPositiveButton("Login") { _, _ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            .show()
                    }else{
                        val accountintent = Intent(this, AccountActivity::class.java)
                        startActivity(accountintent)
                        finish()
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        drawer.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.menu_cart -> {
                    if (userid == 0) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Log In To See Cart Products")
                            .setNeutralButton("cancel") { _, _ ->
                                closeContextMenu()
                            }
                            .setPositiveButton("Login") { _, _ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            .show()
                    }else{
                        val cartintent = Intent(this, CartActivity::class.java)
                        startActivity(cartintent)
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        drawer.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.menu_order -> {
                    if (userid == 0) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Log In To See Your Orders")
                            .setNeutralButton("cancel") { _, _ ->
                                closeContextMenu()
                            }
                            .setPositiveButton("Login") { _, _ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            .show()
                    }else{
                        val orderintent = Intent(this, OrderActivity::class.java)
                        startActivity(orderintent)
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        drawer.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.menu_logout -> {
                    pref.edit().remove("UserID").clear().apply()
                    Toast.makeText(this, "Logged Out SuccessFully", Toast.LENGTH_SHORT).show()
                    this.recreate()
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
        CoroutineScope(Dispatchers.IO).launch {
            Profile.get(userid).also {
                if(it!=null)
                {
                    withContext(Dispatchers.Main){
                        if (it.Gender == "male") {
                            malegif.visibility = View.VISIBLE
                            femalegif.visibility = View.GONE
                        } else {
                            femalegif.visibility = View.VISIBLE
                            malegif.visibility = View.GONE
                        }
                        email.text=it.Email
                        name.text=it.Name
                    }
                }
                else
                {
                    withContext(Dispatchers.Main){
                        email.visibility = View.GONE
                        name.text=getString(R.string.completeprofile)
                    }
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val categories = Product.getCategories()
            if (categories.isNotEmpty()) {
                    for (img in categories)
                        Productsimg.downloadcategoryimages(this@HomeActivity, img)

                withContext(Dispatchers.Main) {
                    categorygrid.adapter = CategoryGridAdapter(this@HomeActivity, categories)
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val maleproduct = Product.getProductsForGender(true)
            if (maleproduct.isNotEmpty()) {
                for (img in maleproduct)
                    Productsimg.downloadallproductimages(this@HomeActivity, img)

                withContext(Dispatchers.Main) {
                    maletab.adapter = ProductGridAdapter(this@HomeActivity, maleproduct)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val femaleproduct = Product.getProductsForGender(false)
            if (femaleproduct.isNotEmpty()) {
                for (img in femaleproduct)
                    Productsimg.downloadallproductimages(this@HomeActivity, img)

                withContext(Dispatchers.Main) {
                    femaletab.adapter = ProductGridAdapter(this@HomeActivity, femaleproduct)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val product = Product.getAll()
            if (product.isNotEmpty()) {
                for (img in product)
                    Productsimg.downloadallproductimages(this@HomeActivity, img)

                withContext(Dispatchers.Main) {
                    productgrid.adapter = ProductGridAdapter(this@HomeActivity, product)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_items, menu)

        val loginmenuItem = menu.findItem(R.id.login)
        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        loginmenuItem.isVisible = userid == 0

        return true
    }

    private var imageListener: ImageListener =
        ImageListener { position, imageView -> // You can use Glide or Picasso here
            imageView.setImageResource(sampleImages[position])
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        when (item.itemId) {
            android.R.id.home -> {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START)
                else
                    drawer.openDrawer(GravityCompat.START)
            }
            R.id.btncart -> {
                if (userid == 0) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Log In To See Cart Products")
                        .setNeutralButton("cancel") { _, _ ->
                            closeContextMenu()
                        }
                        .setPositiveButton("Login") { _, _ ->
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .show()
                }else{
                    val cartintent = Intent(this, CartActivity::class.java)
                    startActivity(cartintent)
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    return true
                }
            }
            R.id.login -> {
                val loginintent = Intent(this, LoginActivity::class.java)
                startActivity(loginintent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
