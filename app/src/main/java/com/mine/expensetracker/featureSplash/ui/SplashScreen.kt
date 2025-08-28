package com.mine.expensetracker.featureSplash.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.mine.expensetracker.R
import com.mine.expensetracker.featureSplash.viewmodel.SplashViewModel
import com.mine.expensetracker.utils.AppUtils

//SplashScreen - Could be used to init firebase service etc.
class SplashScreen : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AppUtils.styleSystemBars(this@SplashScreen, backgroundColor = ContextCompat.getColor(this, R.color.theme_color), false,true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_ui)

        //view model
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        //lottie animation
        val lottieView = findViewById<LottieAnimationView>(R.id.lottieView)
        lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                identifyNextScreen()
            }
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    // Identify next screen based on login status
    fun identifyNextScreen(){
        val next = viewModel.getNextScreen(this)
        startActivity(Intent(this, next))
        finish()
    }
}