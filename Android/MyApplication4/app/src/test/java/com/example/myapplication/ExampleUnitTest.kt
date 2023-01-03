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
        val a1=A(1)
        val a2=A(2)
        val a3=A(3)
        val list= mutableListOf<A>()
        list.add(a3)
        list.add(a2)
        list.add(a1)
        list.forEach {
            println(it.time)
        }
        println("-------------")
        list.sortBy { it.time }
        list.forEach {
            println(it.time)
        }
    }
    class A(var time:Long){
    }
}