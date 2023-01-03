package com.example.gobanggame

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gobanggame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.customView.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = e.x
                    val y = e.y
                    val x_n: Int = ((x - 20) / 60f + 0.5f).toInt()
                    val y_n: Int = ((y - 20) / 60f + 0.5f).toInt()
                    val data = Data(Point(x_n, y_n))
                    binding.customView.addData(data)
                    binding.customView.invalidate()
                    if (binding.customView.wSuccess) {
                        Toast.makeText(this, "白旗获胜！", Toast.LENGTH_LONG).show()
                        binding.customView.setEnabled(false)
                    } else if (binding.customView.bSuccess) {
                        Toast.makeText(this, "黑棋获胜！", Toast.LENGTH_LONG).show()
                        binding.customView.setEnabled(false)
                    }
                }
            }
            return@setOnTouchListener true
        }
        binding.reset.setOnClickListener {
            binding.customView.reset()
            binding.customView.invalidate()
            binding.customView.setEnabled(true)
        }
    }
}

