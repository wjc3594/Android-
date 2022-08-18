package com.example.myapplication

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {
    private lateinit var view: ScrollImageView
    private lateinit var bitmap: Bitmap
    private var offset = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.view)
        view.postDelayed( {
            bitmap = zoom(view.width)
            view.setParams(bitmap)
        },6000)
    }

    private fun zoom(w: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.login_scroller_bg)
        val width = bitmap.width
        val height = bitmap.height
        val scale = (w.toFloat()) / width
        val newHeight = (height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, w, newHeight, true)
    }
}