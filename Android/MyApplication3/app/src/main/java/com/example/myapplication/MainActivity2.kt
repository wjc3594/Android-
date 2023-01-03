package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {


    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textView=findViewById(R.id.text)
        textView.setOnClickListener {
            Toast.makeText(this,"清理缓存！", Toast.LENGTH_SHORT).show()
            Log.e("wjc","清理缓存")
        }
    }
}