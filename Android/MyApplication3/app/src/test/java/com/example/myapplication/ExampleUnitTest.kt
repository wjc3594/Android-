package com.example.myapplication

import org.junit.Test
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //assertEquals(4, 2 + 2)
//        runBlocking {
//            val job=GlobalScope.launch {
//                System.out.println("1")
//                delay(500)
//                System.out.println("2")
//            }
//            System.out.println(job.isActive)
//            delay(1000)
//            System.out.println(job.isActive)
//        }
       System.out.println(isNum("04139493147912"))


    }

    private fun isNum(str:String):Boolean{
        val pattern: Pattern = Pattern.compile("^[0-9]*$")
        val matcher: Matcher = pattern.matcher(str)
        return matcher.matches()

    }
}