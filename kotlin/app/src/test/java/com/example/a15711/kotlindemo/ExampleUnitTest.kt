package com.example.a15711.kotlindemo

import com.example.a15711.kotlindemo.StaticSingleInstance.test
import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import java.lang.Thread.sleep
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val str="data:image/png;base64,AAQSkZJRgABAQEASABIAAD2wBDAAoHBwgHBgoI"
//        val res=Pattern.compile("data:.*/.*;base64,")
        val index=str.indexOf(";base64,")

        if(index!=-1){
            println(str.substring(index+8))
//            println(index)
        }else{
            println(str)
        }
//        val res=Pattern.compile("data:.*/.*;base64,")
//        val result=str.split(res)
//
//        result.forEach {
//            println("----$it")
//        }
        //1.GlobalScope.launch
//        GlobalScope.launch {
//            delay(1500)
//            println("codes run in coroutine scope")
//        }
//        sleep(1000)
        //2.runBlocking
//        runBlocking {
//            launch {
//                delay(1000)
//                println("codes run in runblocking")
//                method1()
//                method2()
//            }
//        }
        //3.CoroutineScope
//        val job=Job()
//        val scope= CoroutineScope(job)
//        scope.launch {
//            println("111111")
//            delay(100)
//            println("111111")
//        }
//        scope.launch {
//            println("22222")
//            delay(100)
//            println("22222")
//        }
//        //job.cancel()
//        sleep(1000)
        //4.能获取协程结果的async函数
//        runBlocking {
//            val result=async {
//                5+6
//            }.await()
//            println(result)
//        }
        //5.await会阻塞当前协程，直到可以获取到async的结果
//        runBlocking {
//            val result=async {
//                sleep(1000)
//                println("第一个async函数")
//                5+6
//            }.await()
//            async {
//                println("第二个async函数")
//            }.await()
//            println(result)
//        }
        //6.async函数同时执行
//        runBlocking {
//            launch {
//                val start=System.currentTimeMillis()
//                val deferred1=async {
//                    delay(1000)
//                    println("第一个async函数")
//                }.await()
//                val deferred2=async {
//                    delay(1000)
//                    println("第二个async函数")
//                }.await()
////            deferred1.await()
////            deferred2.await()
//                println(System.currentTimeMillis()-start)
//            }
//
//            launch {
//                delay(500)
//                println("第二个launch")
//            }
//
//        }
        //7.强制要求指定线程的协程
//        runBlocking {
//            withContext(Dispatchers.Default){
//                delay(1000)
//                println("withContext"+Thread.currentThread().id)
//            }
//        }
//        println("主线程id=${Thread.currentThread().id}")

//        runBlocking {
//            launch(Dispatchers.IO){
//                println(Thread.currentThread().name)
//                println("1")
//            }
//            println("11")
//            launch(Dispatchers.IO){
//                println(Thread.currentThread().name)
//                println("2") }
//            println("22")
//            launch{
//                println(Thread.currentThread().name)
//                println("3") }
//            println("33")
        }

    suspend fun method1(){
        delay(1000)
        println("test  fdfda")
    }
    suspend fun method2()= coroutineScope{
        launch {
            println("method2")
        }
    }
//    //常见的网络请求回调
//    HttpUtil.sendHttpRequest(url,object:HttpCallbackListener{
//        override fun onFinish(response:String){
//            //得到服务器返回的具体内容
//        }
//        override fun onError(e:Exception){
//            //异常情况处理
//        }
//    })
//    //使用协程简化回调
//    suspend fun request(url:String):String{
//        return suspendCoroutine { continuation ->
//            HttpUtil.sendHttpRequest(url,object:HttpCallbackListener{
//                override fun onFinish(response:String){
//                    //得到服务器返回的具体内容
//                    continuation.resume(response)
//                }
//                override fun onError(e:Exception){
//                    //异常情况处理
//                    continuation.resumeWithException(e)
//                }
//            })
//        }
//    }
//    //后续网络请求可直接这么写
//    try{
//        val response=request("xxxx")
//    }catch(e:Exception){
//
//    }
}
