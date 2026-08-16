package com.collabera.weather.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.collabera.weather.R
import com.collabera.weather.databinding.ActivitySplashScreenBinding
import com.collabera.weather.presentation.ui.loginReg.LoginAc

class SplashAc : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val topAnim = AnimationUtils.loadAnimation(this , R.anim.down_from_top)
        binding.RLTop!!.startAnimation(topAnim)
        val bottomAnim = AnimationUtils.loadAnimation(this , R.anim.bottom_down)
        binding.RLBottom!!.startAnimation(bottomAnim)
        bottomAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {

                nextTo()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun nextTo() {
        startActivity(Intent(this , LoginAc::class.java))
        finish()
    }
}