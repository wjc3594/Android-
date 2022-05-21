package com.example.a15711.kotlindemo.StaticSingleInstance.GenericParadigm

open class HuaweiController<T : TV> : Controller<T> {
    override fun open(t: T) {
        t.play()
    }

    override fun close(t: T) {
        t.stop()
    }

}