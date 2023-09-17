package com.example.mypokemon.baseclasses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//BaseClass of activity which is extended with derived class
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}