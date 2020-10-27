package com.example.myapplication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.auth.LoginActivity
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val lottieAnimationView:LottieAnimationView = findViewById(R.id.lottie)

        lottieAnimationView.animate().translationY(1600F).setDuration(1000).setStartDelay(5000);

        Handler().postDelayed({
            startActivity(
            Intent(this, LoginActivity::class.java),
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            ).toBundle())
        }, 6000)
    }
}
