package com.example.a15711.kotlindemo.StaticSingleInstance

/**
 * 普通类中可利用伴生对象，实现将部分方法转变为类似静态方法的形式
 */
class CompanionClass {
    companion object {
        fun test(): String {
            return "I am companion method"
        }
    }

    fun test(): String {
        return "I am 普通方法"
    }
}