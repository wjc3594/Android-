package com.example.a15711.kotlindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a15711.kotlindemo.ExtendClass.isBig
import com.example.a15711.kotlindemo.StaticSingleInstance.CompanionClass
import com.example.a15711.kotlindemo.StaticSingleInstance.SingleInstance
import com.example.a15711.kotlindemo.StaticSingleInstance.test
import com.example.a15711.kotlindemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btn.setOnClickListener {
            /**
             * run\with\apply
             */
//            button.text = RunApplyWith.resultRun
//            button.text = RunApplyWith.resultWith
//            button.text=RunApplyWith.resultApply.toString()
            /***
             * 扩展类方法
             */
            binding.btn.text = "wjc".isBig().toString()
        }
        /**
         * 顶层方法
         */
        test()
        /**
         * 单例类方法
         */
        SingleInstance.test()
        /**
         * 伴生方法
         */
        CompanionClass.test()
    }
}
