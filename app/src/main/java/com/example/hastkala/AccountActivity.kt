package com.example.hastkala

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.hastkala.api.Profile
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifImageView

class AccountActivity : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var mobileno: TextView

    private lateinit var edit: Button
    private lateinit var cartview: Button
    private lateinit var orderview: Button

    private lateinit var malegif: GifImageView
    private lateinit var femalegif: GifImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        supportActionBar?.title = "Account"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = findViewById(R.id.name)
        email = findViewById(R.id.useremail)
        mobileno = findViewById(R.id.number)

        malegif = findViewById(R.id.Malecardview)
        femalegif = findViewById(R.id.femalecardview)

        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val profile = Profile.get(userid)
            if (profile != null) {
                withContext(Dispatchers.Main) {
                    name.text = profile.Name
                    email.text = profile.Email
                    mobileno.text = profile.MobileNumber
                    if (profile.Gender == "male") {
                        malegif.visibility = View.VISIBLE

                        femalegif.visibility = View.GONE
                    } else {
                        femalegif.visibility = View.VISIBLE

                        malegif.visibility = View.GONE
                    }
                }
            }
            else
            {
                withContext(Dispatchers.Main){

                    name.text = getString(R.string.incompletedata)
                    email.visibility = View.GONE
                    mobileno.visibility = View.GONE
                    femalegif.visibility = View.GONE

                    malegif.visibility = View.GONE
                }
            }
        }
        edit = findViewById(R.id.Edit_Profile)
        cartview = findViewById(R.id.show_cart)
        orderview = findViewById(R.id.show_order)
        edit.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout)

        }
        cartview.setOnClickListener {
            val cartintent = Intent(this, CartActivity::class.java)
            startActivity(cartintent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }
        orderview.setOnClickListener {
            val orderintent = Intent(this, OrderActivity::class.java)
            startActivity(orderintent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home)
            finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        return super.onOptionsItemSelected(item)

    }
}