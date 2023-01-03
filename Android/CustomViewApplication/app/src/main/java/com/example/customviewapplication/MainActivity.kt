package com.example.customviewapplication

import android.graphics.Outline
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import androidx.databinding.DataBindingUtil
import com.example.customviewapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //改变轮廓
        binding.image.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                //支持原型、矩形、圆角矩形、路径(试了没效果)
                if (outline?.canClip() == true){
                    view?.let {
                        outline.setOval(0,0,view.width,view.height)
                        Log.i("wjc","success")
                    }
                }
            }
        }
        //跟据轮廓进行裁剪
        binding.image.clipToOutline = true
    }
}