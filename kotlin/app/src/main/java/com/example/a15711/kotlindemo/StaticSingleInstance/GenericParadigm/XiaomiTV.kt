package com.example.a15711.kotlindemo.StaticSingleInstance.GenericParadigm

import android.util.Log

class XiaomiTV : TV {
    private val TAG = "XiaomiTV"
    override fun play() {
        Log.d(TAG, "play")
    }

    override fun stop() {
        Log.d(TAG, "stop")
    }

}