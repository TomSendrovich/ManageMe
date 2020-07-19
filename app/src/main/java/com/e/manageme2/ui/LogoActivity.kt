package com.e.manageme2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.e.manageme2.R
import kotlinx.android.synthetic.main.activity_logo.*
import kotlinx.android.synthetic.main.activity_main.*

class LogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        ic_logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_splash_in))
        Handler().postDelayed({
            ic_logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_splash_out))
            Handler().postDelayed({
                ic_logo.visibility = View.GONE
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            },500)
        },1500)
    }
}