package com.example.a15711.kotlindemo.Coroutine

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 协程，在IO时绑定的线程依旧会阻塞，使用协程可以简化回调和切换线程
 */
fun main() {
    runBlocking {
        val firstDispatcher = Executors.newSingleThreadExecutor {
            Thread(it, "First")
        }.asCoroutineDispatcher()
        var thread: Thread? = null
        launch(firstDispatcher) {
            thread = Thread.currentThread()
            println("after test11,thread.name=${thread?.name},thread.state=${thread?.state}")
            write()
            println("after test12,thread.name=${thread?.name},thread.state=${thread?.state}")
        }
//        delay(18)
        Thread.sleep(8)
        println("after test2,current_thread.name=${Thread.currentThread().name}")
        /**
         * 1、不指定firstDispatcher时，test3 log在write start和write end之间打印
         * 2、当指定firstDispatcher时，test3 log在write end后打印，说明协程遇到io时所绑定的线程一样会阻塞
         */
        launch() {
            println("test3,thread.name=${thread?.name},thread.state=${thread?.state}")
            println("test3,current_thread.name=${Thread.currentThread().name}")
        }
    }

}

private fun write() {
    println("write thread.name-start=${Thread.currentThread().name}")
    val start = System.currentTimeMillis()
    val file = File("wjc-test")
    if (!file.exists()) {
        file.createNewFile()
    }
    val stream = FileOutputStream(file, true)
    val bytes = ByteArray(10000000) {
        it.toByte()
    }

    stream.write(bytes)
    stream.flush()
    stream.close()
    val end = System.currentTimeMillis()
    println("write thread.name-end=${Thread.currentThread().name}")
    println("total time=${end - start}")
}