package com.example.myapplication

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Test
import java.util.concurrent.Executors

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)\
        A.bb()
        val b="99999"
        val c=(A.a!=b)
        println(c)
    }
    object A{
        init {
            println("init")
        }

        val a="99999"
        fun bb(){
            println("bb")
        }
    }
}