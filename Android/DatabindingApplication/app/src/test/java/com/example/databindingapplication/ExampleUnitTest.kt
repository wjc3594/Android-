package com.example.databindingapplication

import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var num=AtomicInteger(0)
        val runnable=Runnable{
            for(i in 0..1000000000){
                num.getAndAdd(1)
            }
            println("执行结束11")
        }
        val t1=Thread(runnable)
        val t2=Thread(runnable)
        t1.start()
        t2.start()
        Thread.sleep(1000)
        println("num=$num")
    }
}