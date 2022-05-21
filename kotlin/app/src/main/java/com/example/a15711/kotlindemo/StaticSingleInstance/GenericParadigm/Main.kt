package com.example.a15711.kotlindemo.StaticSingleInstance.GenericParadigm

class Main {
    private fun test(t: Num<Number>) {
        t.println(2)
    }

    fun main() {
        test(Num<Any>())
    }
}