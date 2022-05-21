package com.example.a15711.kotlindemo.StaticSingleInstance.GenericParadigm

interface Controller<T> {
    fun open(t: T)

    fun close(t: T)
}