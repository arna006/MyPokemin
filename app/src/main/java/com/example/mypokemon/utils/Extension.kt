package com.example.mypokemon.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.mypokemon.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat


inline fun <reified T> Activity.startNewActivity(finish: Boolean) {
    startActivity(Intent(this, T::class.java))
    if (finish) {
        finish()
    }
}

inline fun <reified T> Activity.startNewActivity(
    finish: Boolean,
    values: (Intent) -> Unit
) {
    startActivity(Intent(this, T::class.java).also {
        values(it)
    })
    if (finish) {
        finish()
    }
}

fun ImageView.setImage(imageId: Any?, error: Any = android.R.drawable.ic_menu_report_image) {

    Glide.with(this).load(imageId).error(error).into(this)
}


fun Context.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}

fun View.beGone() {
    this.visibility = View.GONE
}



fun View.beVisible() {
    this.visibility = View.VISIBLE
}


fun String.showLog(message: String?) {
    message?.let {
        Log.d(this, it)
    }
}



fun Context.getColorFromResource(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}






