package com.example.a15711.kotlindemo.Coroutine

import kotlinx.coroutines.delay

class CoroutineTest1 {
    companion object {
        suspend fun test() {
            delay(500)
            println("test")
        }
    }

}