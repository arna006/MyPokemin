package com.example.mypokemon.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mypokemon.R
import com.example.mypokemon.baseclasses.BaseActivity
import com.example.mypokemon.utils.startNewActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Wait user about 3000 milli sec which are equal to 3 sec
        Handler(mainLooper).postDelayed({
            startNewActivity<MainActivity>(false)
            finish()
        }, 3000L)
    }
}