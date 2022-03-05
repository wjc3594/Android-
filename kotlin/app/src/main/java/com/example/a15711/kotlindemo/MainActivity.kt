package com.example.a15711.kotlindemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.a15711.kotlindemo.ExtendClass.isBig
import com.example.a15711.kotlindemo.StaticSingleInstance.CompanionClass
import com.example.a15711.kotlindemo.StaticSingleInstance.SingleInstance
import com.example.a15711.kotlindemo.StaticSingleInstance.test
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            /**
             * run\with\apply
             */
//            button.text = RunApplyWith.resultRun
//            button.text = RunApplyWith.resultWith
//            button.text=RunApplyWith.resultApply.toString()
            /***
             * 扩展类方法
             */
            button.text = "wjc".isBig().toString()
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
