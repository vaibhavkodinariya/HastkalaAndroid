package com.example.hastkala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.content.Intent
import com.example.hastkala.LoginActivity
import com.example.hastkala.R


class SplashActivity : AppCompatActivity() {
    private lateinit var ring: ImageView
    private lateinit var man: ImageView
    private lateinit var anim: Animation
    private lateinit var anim_man: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide();
        val end_splash = Intent(this, HomeActivity::class.java)
        ring = findViewById(R.id.ring)
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate)

        man = findViewById(R.id.man)
        anim_man = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_spash)

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startActivity(end_splash)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        ring.startAnimation(anim)

        man.startAnimation(anim_man)

    }
}
