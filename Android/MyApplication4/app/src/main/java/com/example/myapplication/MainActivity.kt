package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.MessageQueue
import android.util.Log
import android.util.LogPrinter
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        Looper.getMainLooper().setMessageLogging(LogPrinter(Log.INFO,"wjc1"))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Looper.getMainLooper().queue.addIdleHandler(MessageQueue.IdleHandler {
            Log.i("wjc2","idleHandler queueIdle")
            return@IdleHandler false
        })
    }
}