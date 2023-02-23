package com.example.hastkala

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hastkala.api.Profile
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var slideLeftMale: ObjectAnimator
    private lateinit var slideRightFemale: ObjectAnimator
    private lateinit var slideRightMale: ObjectAnimator
    private lateinit var slideLeftFemale: ObjectAnimator

    private lateinit var cardMale: CircularRevealCardView
    private lateinit var cardFemale: CircularRevealCardView

    private lateinit var name: TextInputEditText
    private lateinit var mobileno: TextInputEditText
    private lateinit var address: TextInputEditText
    private lateinit var state: TextInputEditText
    private lateinit var pincode: TextInputEditText

    private lateinit var btnsave: Button
    private lateinit var btnupdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = "Edit Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cardMale = findViewById(R.id.malecardview)
        cardFemale = findViewById(R.id.femalecardview)

        val pref = getSharedPreferences("hastkala", MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)

        name = findViewById(R.id.Username)
        mobileno = findViewById(R.id.Usermobileno)
        address = findViewById(R.id.useraddress)
        state = findViewById(R.id.userstate)
        pincode = findViewById(R.id.userpincode)

        slideLeftMale =
            ObjectAnimator.ofFloat(cardMale, "translationX", -250.0f).apply { duration = 1000 }
        slideRightFemale =
            ObjectAnimator.ofFloat(cardFemale, "translationX", 250.0f).apply { duration = 1000 }

        slideRightMale =
            ObjectAnimator.ofFloat(cardMale, "translationX", 0.0f).apply { duration = 1000 }
        slideLeftFemale =
            ObjectAnimator.ofFloat(cardFemale, "translationX", 0.0f).apply { duration = 1000 }

        findViewById<Button>(R.id.EditGender).setOnClickListener {
            cardMale.setOnClickListener(::cardMaleClicked)
            cardFemale.setOnClickListener(::cardFemaleClicked)

            slideLeftMale.start()
            slideRightFemale.start()

        }
        btnsave = findViewById(R.id.save)
        btnupdate = findViewById(R.id.update)
        CoroutineScope(Dispatchers.IO).launch {
            val profile = Profile.get(userid)
            if (profile != null) {
                if (profile.Gender == "male") {
                    cardMale.z = 1.0f
                    cardFemale.z = 0.0f
                } else {
                    cardMale.z = 0.0f
                    cardFemale.z = 1.0f
                }
                withContext(Dispatchers.Main)
                {
                    name.setText(profile.Name)
                    mobileno.setText(profile.MobileNumber)
                    address.setText(profile.Address)
                    state.setText(profile.State)
                    pincode.setText(profile.Pincode)
                    btnupdate.visibility = View.VISIBLE
                    btnsave.visibility = View.GONE

                }
            } else {
                withContext(Dispatchers.Main) {
                    btnsave.visibility = View.VISIBLE
                    btnupdate.visibility = View.GONE
                }
            }

            btnsave.setOnClickListener {
                val name = name.text.toString()
                val mobileno = mobileno.text.toString()
                val address = address.text.toString()
                val state = state.text.toString()
                val pincode = pincode.text.toString()
                val gender = if (cardMale.z == 1.0f && cardFemale.z == 0.0f) {
                    cardMale.contentDescription.toString()
                } else {
                    cardFemale.contentDescription.toString()
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val messege =
                        Profile.saveprofile(userid, name, mobileno, address, state, pincode, gender)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProfileActivity,
                            messege.getString("Messege"),
                            Toast.LENGTH_SHORT
                        ).show()
                        this@ProfileActivity.recreate()
                    }
                }
            }
            btnupdate.setOnClickListener {
                val name = name.text.toString()
                val mobileno = mobileno.text.toString()
                val address = address.text.toString()
                val state = state.text.toString()
                val pincode = pincode.text.toString()
                val gender = if (cardMale.z == 1.0f && cardFemale.z == 0.0f) {
                    cardMale.contentDescription.toString()
                } else {
                    cardFemale.contentDescription.toString()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val messege =
                        Profile.updateprofile(
                            userid,
                            name,
                            mobileno,
                            address,
                            state,
                            pincode,
                            gender
                        )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProfileActivity,
                            messege.getString("Messege"),
                            Toast.LENGTH_SHORT
                        ).show()
                        this@ProfileActivity.recreate()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
            val intent=Intent(this,AccountActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cardMaleClicked(view: View) {
        slideRightMale.start()
        slideLeftFemale.start()
        cardMale.z = 1.0f
        cardFemale.z = 0.0f

        cardMale.setOnClickListener(null)
        cardFemale.setOnClickListener(null)
    }

    private fun cardFemaleClicked(view: View) {
        slideRightMale.start()
        slideLeftFemale.start()
        cardFemale.z = 1.0f
        cardMale.z = 0.0f

        cardFemale.setOnClickListener(null)
        cardMale.setOnClickListener(null)
    }
}
